package com.github.netty.learning.productionIdel.acceptor;

import com.github.netty.learning.productionIdel.common.Acknowledge;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.github.netty.learning.productionIdel.common.NettyCommonProtocol.ACK;
import static com.github.netty.learning.productionIdel.common.NettyCommonProtocol.MAGIC;
import static com.github.netty.learning.productionIdel.serializer.SerializerHolder.serializerImpl;


@ChannelHandler.Sharable
public class AcknowledgeEncoder extends MessageToByteEncoder<Acknowledge> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Acknowledge ack, ByteBuf out) throws Exception {
        byte[] bytes = serializerImpl().writeObject(ack);
        out.writeShort(MAGIC)
                .writeByte(ACK)
                .writeByte(0)
                .writeLong(ack.sequence())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
