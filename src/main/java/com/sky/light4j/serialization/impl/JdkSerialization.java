package com.sky.light4j.serialization.impl;

import com.sky.light4j.serialization.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;

/**
 * Created by shaojunjie on 2015/3/13.
 */
public class JdkSerialization implements Serialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdkSerialization.class);

    @Override
    public <T> byte[] serialize(T obj) {

        ByteArrayOutputStream bout=null;
        ObjectOutputStream oout=null;
        try {
            bout= new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeObject(obj);
            oout.flush();
            byte[] bytes=bout.toByteArray();
            LOGGER.debug("jdk序列化大小为："+bytes.length);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (oout != null) {
                try {
                    oout.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }

        }
           return null;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {

        ByteArrayInputStream bin= new ByteArrayInputStream(data);
        ObjectInputStream oin=null;
        try {
            oin=new ObjectInputStream(bin);
            return  (T)oin.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (oin != null) {
                try {
                    oin.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }


}
