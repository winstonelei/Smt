package com.github.netty.learning.productionIdel.acceptor;

import java.net.SocketAddress;

public interface SrvAcceptor {
	
	SocketAddress localAddress();
	
	void start() throws InterruptedException;
	
	void shutdownGracefully();
	
	void start(boolean sync) throws InterruptedException;

}
