package com.sky.light4j.serialization.impl;


import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.sky.light4j.serialization.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by shaojunjie on 2015/3/20.
 */
public class HessionSerialization implements Serialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(HessionSerialization.class);

    @Override
    public <T> byte[] serialize(T obj) {

        ByteArrayOutputStream bout=null;
        Hessian2Output oout=null;
        try {
            bout= new ByteArrayOutputStream();
            oout = new Hessian2Output(bout);
            oout.writeObject(obj);
            oout.flush();
            byte[] bytes=bout.toByteArray();
            LOGGER.debug("hession序列化大小为："+bytes.length);
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
        Hessian2Input oin=null;
        try {
            oin=new Hessian2Input(bin);
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
