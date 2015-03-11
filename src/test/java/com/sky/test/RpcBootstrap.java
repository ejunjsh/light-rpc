package com.sky.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shaojunjie on 2015/3/2.
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("server.xml");
    }
}
