package com.github.guavaFuture.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by winstone on 2017/6/20.
 */
public class BlockingPolicy implements RejectedExecutionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BlockingPolicy.class);

    private String threadName;

    public BlockingPolicy() {
        this(null);
    }

    public BlockingPolicy(String threadName) {
        this.threadName = threadName;
    }

    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(runnable);
            } catch (InterruptedException e) {
            }
        }
    }

}
