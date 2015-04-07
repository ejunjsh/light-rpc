package com.sky.test;

import com.sky.light4j.annotation.RpcService;

/**
 * Created by shaojunjie on 2015/3/2.
 */
@RpcService(HelloService.class) // 指定远程接口
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }


//    private static int kl=3;
//    public test3 tt=new test3();
//
//    public void hhh()
//    {
//
//    }
//
//    private  class test3
//    {
//        private int kk;
//
//        private   void teea()
//        {
//
//        }
//    }
}
