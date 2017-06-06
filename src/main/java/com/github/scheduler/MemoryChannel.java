package com.github.scheduler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2016/12/6.
 * 内存channel
 */
public class MemoryChannel extends AbstractMemoryChannel {
    private LinkedBlockingQueue<Object> queue;

    private Semaphore queueRemaining;

    public MemoryChannel(int capacity){
        super();
        queue = new LinkedBlockingQueue<Object>(capacity);
        queueRemaining = new Semaphore(capacity);
    }


    @Override
    public boolean offer(Object event) {
        try {
            if(queueRemaining.tryAcquire(expire, timeUnit)){
                queue.offer(event);
            }
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object poll(long timeout, TimeUnit unit) {
        try {
            Object event = queue.poll(timeout, unit);
            queueRemaining.release();
            return event;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }


    @Override
    public Object poll() {
        return poll(expire, timeUnit);
    }

}
