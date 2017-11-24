package com.github.guavaFuture;

/**
 * Created by winstone on 2017/6/19.
 */
public class RcfConstants {
    public static final long  SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT = Long.getLong("nettyrpc-default-msg-timeout", 10 * 1000L);

    public static final String SYSTEM_PROPERTY_THREADPOOL_REJECTED_POLICY_ATTR ="com.github.rcf.core.thread.rejectedPolicyType";

    public static final String SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTR ="com.github.rcf.core.thread.blockingQueue";

    public static final String SYSTEM_PROPERITY_ACCESS_ADAPTIVE_PROVIDER ="com.github.rcf.core.compiler.AccessAdaptive";

    public static final int PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());

    public static final int SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS = 1;

    public static final int SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS = 16 ;

    public static final int SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY = 15;

    public static final long SYSTEM_PROPERTY_ASYNC_MESSAGE_CALLBACK_TIMEOUT = 60 * 1000L;
}
