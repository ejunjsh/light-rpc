package com.sky.light4j.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by shaojunjie on 2015/3/2.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    private Serialization serialization;

    public RpcEncoder(Class<?> genericClass,Serialization serialization) {
        this.serialization=serialization;
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serialization.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
