package com.github.netty.learning.productionIdel.connector;

import io.netty.channel.Channel;

/**
 * @description
 */
public interface ClientConnector {
	
	Channel connect(int port, String host);
	
	void shutdownGracefully();
	
}
