package com.github.netty.learning.idle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2017/7/24.
 */
public class HeartBeatServer {

    private final AcceptorIdleStateTrigger  idleStateTrigger = new AcceptorIdleStateTrigger();

    private int port;

    public HeartBeatServer(int port){
        this.port = port;
    }

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroop = new NioEventLoopGroup();

        try {

            ServerBootstrap strap = new ServerBootstrap().group(bossGroup, workGroop)
                    .channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(idleStateTrigger);
                            socketChannel.pipeline().addLast("decoder", new StringDecoder());
                            socketChannel.pipeline().addLast("encoder", new StringEncoder());
                            socketChannel.pipeline().addLast(new HeartBeatServerHandler());

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture future = strap.bind(port).sync();

            System.out.println("Server start listen at " + port);

            future.channel().closeFuture().sync();

        } catch(Exception e){
                bossGroup.shutdownGracefully();
                workGroop.shutdownGracefully();
        }


    }


    public static void main(String[] args){

        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new HeartBeatServer(port).start();

    }
}
