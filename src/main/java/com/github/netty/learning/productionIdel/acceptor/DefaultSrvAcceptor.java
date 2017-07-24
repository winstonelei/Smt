package com.github.netty.learning.productionIdel.acceptor;

import com.github.netty.learning.productionIdel.common.NettyEvent;
import com.github.netty.learning.productionIdel.common.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class DefaultSrvAcceptor extends NettySrvAcceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultCommonSrvAcceptor.class);
	
	protected final NettyEventExecuter nettyEventExecuter = new NettyEventExecuter();
	
	public void putNettyEvent(final NettyEvent event) {
        this.nettyEventExecuter.putNettyEvent(event);
    }

	public DefaultSrvAcceptor(SocketAddress localAddress) {
		super(localAddress);
	}
	
	class NettyEventExecuter extends ServiceThread {
        private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
        private final int MaxSize = 10000;


        public void putNettyEvent(final NettyEvent event) {
            if (this.eventQueue.size() <= MaxSize) {
                this.eventQueue.add(event);
            }
            else {
            	logger.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(),
                    event.toString());
            }
        }

        public void run() {
        	logger.info(this.getServiceName() + " service started");

            final ChannelEventListener listener = DefaultSrvAcceptor.this.getChannelEventListener();

            while (!this.isStoped()) {
                try {
                    NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                    if (event != null && listener != null) {
                        switch (event.getType()) {
                        case IDLE:
                            listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CLOSE:
                            listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CONNECT:
                            listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                            break;
                        case EXCEPTION:
                            listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                            break;
                        default:
                            break;

                        }
                    }
                }
                catch (Exception e) {
                	logger.warn(this.getServiceName() + " service has exception. ", e);
                }
            }

            logger.info(this.getServiceName() + " service end");
        }


        @Override
        public String getServiceName() {
            return NettyEventExecuter.class.getSimpleName();
        }
    }

	protected abstract ChannelEventListener getChannelEventListener();

}
