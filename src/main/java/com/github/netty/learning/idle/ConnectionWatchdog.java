package com.github.netty.learning.idle;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2017/7/24.
 */
public abstract  class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask,ChannelHandlerHolder{

    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;
    private final String host;

    private volatile  boolean reconnect = true;
    private int attempts;


    public ConnectionWatchdog(Bootstrap strap,Timer timer,int port,String host,boolean reconnect){
        this.bootstrap = strap;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");
        attempts = 0;
        ctx.fireChannelActive();
    }


    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("链接关闭");
        if(reconnect){
            System.out.println("链接关闭，将进行重连");
            if(attempts < 12){
                attempts++;
                //
                int timeout = 2 << attempts;
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelInactive();
    }


    public void run(Timeout timeOut) throws  Exception{
        ChannelFuture future;
        //bootstrap已经初始化好了，只需要将handler填入就可以了
        synchronized (bootstrap){
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host,port);
        }
        //future对象
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                boolean succed = channelFuture.isSuccess();
                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
                if(!succed){
                    System.out.println("重连失败");
                    channelFuture.channel().pipeline().fireChannelInactive();
                }else{
                    System.out.println("重连成功");
                }
            }
        });

    }

}
