package com.sky.test;

import com.sky.light4j.proxy.Light4jProxyFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by shaojunjie on 2015/3/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client.xml")
public class HelloServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloServiceTest.class);

    @Autowired
    private Light4jProxyFactory rpcProxy;

    @Test
    public void helloTest() {
        long start=System.currentTimeMillis();
        HelloService helloService = rpcProxy.create(HelloService.class);
        for(int i=0;i<=10000;i++) {
            LOGGER.debug("test");
            String result=helloService.hello("邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰邵俊杰");
            LOGGER.debug(result+"====="+i);
        }
        long end=System.currentTimeMillis();

        LOGGER.debug("花费时间:"+(end-start)+"ms");

//        try {
//            Thread.sleep(61000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        String result = helloService.hello("World");
//        LOGGER.debug(result);
//        Assert.assertEquals("Hello! World", result);
    }
}
