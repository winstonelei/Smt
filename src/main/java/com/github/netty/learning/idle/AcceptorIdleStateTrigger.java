package com.github.netty.learning.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by winstone on 2017/7/24.
 */
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext context,Object obj)throws  Exception{
        if(obj instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)obj).state();
            if(state == IdleState.READER_IDLE){
               throw new Exception("idle Exception");
            }
        }else{
            super.userEventTriggered(context,obj);
        }


    }
}
