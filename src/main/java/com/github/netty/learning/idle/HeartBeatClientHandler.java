package com.github.netty.learning.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

/**
 * Created by winstone on 2017/7/24.
 */
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctxt) throws  Exception{
        System.out.println("激活时间是："+new Date());
        System.out.println("HeartBeatClientHandler channelActive");
        ctxt.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctxt)throws  Exception{
        System.out.println("停止时间是："+new Date());
        System.out.println("HeartBeatClientHandler channelInactive");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctxt,Object msg)throws  Exception{
        String message = (String)msg;
        if(message.equals("heartbeat")){
            ctxt.write("has read message from server");
            ctxt.flush();
        }
        ReferenceCountUtil.release(msg);
    }


}
