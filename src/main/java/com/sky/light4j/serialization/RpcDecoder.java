package com.sky.light4j.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by shaojunjie on 2015/3/2.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    private Serialization serialization;

    public RpcDecoder(Class<?> genericClass,Serialization serialization) {
        this.genericClass = genericClass;
        this.serialization=serialization;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            return;
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = serialization.deserialize(data, genericClass);
        out.add(obj);
    }
}
