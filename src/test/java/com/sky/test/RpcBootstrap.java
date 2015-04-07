package com.sky.test;

import com.sky.light4j.server.RpcServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shaojunjie on 2015/3/2.
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context= new ClassPathXmlApplicationContext("server.xml");
        RpcServer server=(RpcServer)context.getBean("rpcServer");
        server.start();
    }
}
