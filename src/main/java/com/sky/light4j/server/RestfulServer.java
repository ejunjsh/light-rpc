package com.sky.light4j.server;

import com.sky.light4j.beans.RpcRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by shaojunjie on 2015/3/20.
 */
@ChannelHandler.Sharable
public class RestfulServer extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware{

    public void start() {

    }


    public void stop() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {

    }
}
