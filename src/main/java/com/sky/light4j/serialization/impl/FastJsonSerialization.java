package com.sky.light4j.serialization.impl;

import com.alibaba.fastjson.JSON;
import com.sky.light4j.serialization.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shaojunjie on 2015/3/13.
 */
public class FastJsonSerialization implements Serialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonSerialization.class);

    @Override
    public <T> byte[] serialize(T obj) {
        String str=JSON.toJSONString(obj);
        byte[] bytes=str.getBytes();
        LOGGER.debug("FastJson序列化大小为："+bytes.length);
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        return JSON.parseObject(new String(data),cls);
    }
}
