package com.sky.test;

import com.sky.rpc.annotation.RpcService;

/**
 * Created by shaojunjie on 2015/3/2.
 */
@RpcService(HelloService.class) // 指定远程接口
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}
