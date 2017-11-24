package com.github.guavaFuture;


import com.github.guavaFuture.policy.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by winstone on 2017/6/20.
 */
public class RcfThreadPool {

    private static long monitorDelay = 100L;

    private static long monitorPeriod = 300L;

    private static final Timer timer = new Timer("ThreadPoolMonitor",true);


    private static RejectedExecutionHandler createPolicy(){
        RejectedPolicyType rejectedPolicyType = RejectedPolicyType.fromString(System.getProperty(RcfConstants.SYSTEM_PROPERTY_THREADPOOL_REJECTED_POLICY_ATTR,"BlockingPolicy"));
        switch (rejectedPolicyType){
            case BLOCKING_POLICY:
                return new BlockingPolicy();
            case CALLER_RUNS_POLICY:
                return new CallerRunsPolicy();
            case ABORT_POLICY:
                return new AbortPolicy();
            case REJECTED_POLICY_TYPE:
                return new RejectedPolicy();
            case DISCARDED_POLICY:
                return new DiscardedPolicy();
        }
        return null;
    }

    private static BlockingQueue<Runnable> createBlockingQueue(int queues){
        BlockingQueueType blockingQueueType = BlockingQueueType.fromString(System.getProperty(RcfConstants.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTR,"LinkedBlockingQueue"));
        switch (blockingQueueType){
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingDeque<>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(RcfConstants.PARALLEL * queues);
            case SYNCHROUNS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }
        return null;
    }



    public static Executor getExecutor(int threads,int queues){
       String name = "RcfThradFactory";
       ThreadPoolExecutor executor = new ThreadPoolExecutor(threads,threads,0,TimeUnit.MILLISECONDS,
               createBlockingQueue(queues),new NamedThreadFactory(name,true),createPolicy());
       return  executor;
    }


    /**
     * 通过监控thread线程可以实时的观察到线程的运行状态，可以通过jmx去实时监控服务端处理能力
     * @param threads
     * @param queues
     * @return
     */
    public static Executor getExecutorWithJmx(int threads,int queues){
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) getExecutor(threads, queues);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ThreadPoolStatus status = new ThreadPoolStatus();
                status.setPoolSize(executor.getPoolSize());
                status.setActiveCount(executor.getActiveCount());
                status.setCorePoolSize(executor.getCorePoolSize());
                status.setMaximumPoolSize(executor.getMaximumPoolSize());
                status.setLargestPoolSize(executor.getLargestPoolSize());
                status.setTaskCount(executor.getTaskCount());
                status.setCompletedTaskCount(executor.getCompletedTaskCount());
                //可以通过监控，将线程状态信息发送出去

            }
        },monitorDelay,monitorPeriod);
        return executor;
    }

}
