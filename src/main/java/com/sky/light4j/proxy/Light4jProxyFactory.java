package com.sky.light4j.proxy;

import com.sky.light4j.beans.RpcRequest;
import com.sky.light4j.beans.RpcResponse;
import com.sky.light4j.client.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by shaojunjie on 2015/3/2.
 */
public class Light4jProxyFactory{

    private static final Logger LOGGER = LoggerFactory.getLogger(Light4jProxyFactory.class);


    private RpcClient client;

    public Light4jProxyFactory(RpcClient client) {
        this.client=client;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        LOGGER.debug("test1");
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        Class<?>[] clss= method.getParameterTypes();
                        if(clss!=null) {
                            String[] array=new String[clss.length];
                            for(int i=0;i<array.length;i++)
                            {
                                array[i]=clss[i].getName();
                            }
                            request.setParameterTypes(array);
                        }
                        request.setParameters(args);
                        LOGGER.debug("test2");
                        RpcResponse response = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应

                        if (response.isError()) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                }
        );
    }
}
