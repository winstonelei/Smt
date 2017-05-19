package com.github.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2016/12/6.
 */
public interface Channel {

    boolean offer(Object event);

    Object poll(long timeout, TimeUnit unit);

    Object poll();

    boolean isEmpty();
}
