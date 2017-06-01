package com.github.netty.learning.end1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 *   使用分隔符来处理拆包粘包的问题
 *   @author  winstone
 */
public class Client {

	public static void main(String[] args) throws Exception {
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				ByteBuf buf = Unpooled.copiedBuffer("*_".getBytes());
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
				sc.pipeline().addLast(new StringDecoder());
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();
		cf.channel().writeAndFlush(Unpooled.wrappedBuffer("bbbasfab*_".getBytes()));
		cf.channel().writeAndFlush(Unpooled.wrappedBuffer("ccsdfasfcc*_".getBytes()));
		cf.channel().writeAndFlush(Unpooled.wrappedBuffer("fffff*_".getBytes()));
		//等待客户端端口关闭
		cf.channel().closeFuture().sync();
		group.shutdownGracefully();
		
	}
}
