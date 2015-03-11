package com.sky.rpc.client;

import com.sky.rpc.serialization.RpcDecoder;
import com.sky.rpc.serialization.RpcEncoder;
import com.sky.rpc.beans.RpcRequest;
import com.sky.rpc.beans.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by shaojunjie on 2015/3/2.
 */
@ChannelHandler.Sharable
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private String host;
    private int port;

    private ServiceDiscovery serviceDiscovery;

    private Map<String,RpcResponse> responseMap=new ConcurrentHashMap<String, RpcResponse>();

    private volatile ChannelFuture future;

    private volatile ChannelFuture closeFuture;

    public RpcClient(String serverAddress)
    {
        String[] array = serverAddress.split(":");
        host = array[0];
        port = Integer.parseInt(array[1]);
        initialize();
        connect();

    }

    public RpcClient(ServiceDiscovery serviceDiscovery) {

        this.serviceDiscovery=serviceDiscovery;

            initialize();
            connect();

    }

    private Bootstrap bootstrap;

    private void initialize()
    {
        EventLoopGroup group = new NioEventLoopGroup();

            bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new IdleStateHandler(0,0,60, TimeUnit.SECONDS)) //没请求时60秒断开连接
                                    .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
    }

    private synchronized void connect()
    {
        if(serviceDiscovery!=null) {
            String serverAddress = serviceDiscovery.discover(); // 发现服务
            String[] array = serverAddress.split(":");
            host = array[0];
            port = Integer.parseInt(array[1]);
        }

      future = bootstrap.connect(host, port).syncUninterruptibly();

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        responseMap.put(response.getRequestId(),response) ;

        LOGGER.debug(response.getRequestId()+"====>map");


    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) {
        if (evt instanceof IdleStateEvent) {
            closeFuture=ctx.close();
            LOGGER.debug("close connection after channel have not any activity for 60 seconds.");

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        closeFuture=ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws Exception {

            if(closeFuture!=null)
            {
                closeFuture.sync();
            }

            if(!future.channel().isActive())
            {
                future.channel().eventLoop().shutdownGracefully();
                connect();
            }

            LOGGER.debug("connected");

            future.channel().writeAndFlush(request).sync();

            LOGGER.debug("sent");

            while (responseMap.get(request.getRequestId())==null)
            {

            }

            LOGGER.debug("received");

            RpcResponse result=responseMap.get(request.getRequestId());
            responseMap.remove(request.getRequestId());
            return result;

    }
}
