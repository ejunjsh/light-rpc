package com.sky.light4j.serialization;

/**
 * Created by shaojunjie on 2015/3/13.
 */
public interface Serialization {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> cls);
}
