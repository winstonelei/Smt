package com.github.netty.learning.productionIdel.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static com.github.netty.learning.productionIdel.common.NettyCommonProtocol.HEAD_LENGTH;
import static com.github.netty.learning.productionIdel.common.NettyCommonProtocol.HEARTBEAT;
import static com.github.netty.learning.productionIdel.common.NettyCommonProtocol.MAGIC;


public class Heartbeats {

    private static final ByteBuf HEARTBEAT_BUF;

    static {
        ByteBuf buf = Unpooled.buffer(HEAD_LENGTH);
        buf.writeShort(MAGIC);
        buf.writeByte(HEARTBEAT);
        buf.writeByte(0);
        buf.writeLong(0);
        buf.writeInt(0);
        HEARTBEAT_BUF = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(buf));
    }

    public static ByteBuf heartbeatContent() {
        return HEARTBEAT_BUF.duplicate();
    }
}
