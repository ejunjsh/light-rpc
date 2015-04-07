package com.sky.light4j.client;

import com.sky.light4j.beans.RpcRequest;
import com.sky.light4j.beans.RpcResponse;
import com.sky.light4j.serialization.RpcDecoder;
import com.sky.light4j.serialization.RpcEncoder;
import com.sky.light4j.serialization.Serialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    private Map<String,Channel> channelMap=new ConcurrentHashMap<String, Channel>();

    private Serialization serialization;

    public RpcClient(String serverAddress,Serialization serialization)
    {
        String[] array = serverAddress.split(":");
        host = array[0];
        port = Integer.parseInt(array[1]);
        this.serialization=serialization;
        initialize();
    }

    public RpcClient(ServiceDiscovery serviceDiscovery,Serialization serialization) {

        this.serviceDiscovery=serviceDiscovery;
        this.serialization=serialization;
        initialize();
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
                                    .addLast(new RpcEncoder(RpcRequest.class,serialization)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new RpcDecoder(RpcResponse.class,serialization)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
    }

    private  Channel getChannel()
    {
        if(serviceDiscovery!=null) {
            String serverAddress = serviceDiscovery.discover(); // 发现服务
            String[] array = serverAddress.split(":");
            host = array[0];
            port = Integer.parseInt(array[1]);
            Channel channel=channelMap.get(serverAddress);
            if(channel!=null)
            {
                if(channel.isOpen())
                {
                    return channel;
                }
                else
                {
                    channelMap.remove(serverAddress);
                }
            }
        }

      ChannelFuture future = bootstrap.connect(host, port).syncUninterruptibly();
      channelMap.put(host+":"+port,future.channel());
      return future.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        responseMap.put(response.getRequestId(),response) ;

        LOGGER.debug(response.getRequestId()+"====>map");


    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
            LOGGER.debug("60秒没有请求，关闭连接");

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }

    //超时30秒
    private int timeout=30000;

    public RpcResponse send(RpcRequest request) {
         try {




             LOGGER.debug("connected");

             getChannel().writeAndFlush(request).sync();

             LOGGER.debug("sent");


             long start=System.currentTimeMillis();
             while (responseMap.get(request.getRequestId()) == null) {
                 if(System.currentTimeMillis()-start>=timeout)
                 {
                     RpcResponse result=new RpcResponse();
                     result.setError(new Exception("响应超时"));
                     LOGGER.error("响应超时");
                     return result;
                 }
             }

             LOGGER.debug("received");

             RpcResponse result = responseMap.get(request.getRequestId());
             responseMap.remove(request.getRequestId());
             return result;
         }
         catch (Exception e)
         {
             RpcResponse result=new RpcResponse();
             result.setError(e);
             LOGGER.error("客户端发送错误",e);
             return result;
         }
    }

    public void destroy()
    {
        if(channelMap!=null&&channelMap.size()>0)
        {
            for(Map.Entry<String,Channel> e:channelMap.entrySet())
            {
                e.getValue().close().syncUninterruptibly();
            }
        }
    }
}
