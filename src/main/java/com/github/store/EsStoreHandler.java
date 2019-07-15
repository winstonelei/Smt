package com.github.store;

import com.github.guavaFuture.NamedThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class EsStoreHandler implements  OutputHandler{

    private static final int THREAD_CPU = 8;
    private static final int CRITICAL_NUM = 100;
    private static final int MAX_CACHED = THREAD_CPU * 10240;

    private final ScheduledThreadPoolExecutor executor;
    private final BlockingQueue<InvokeContext> queue = new LinkedBlockingQueue<>(MAX_CACHED);
  //  private final EsClient esClient;


    public EsStoreHandler() {
        this.executor = new ScheduledThreadPoolExecutor(THREAD_CPU, new NamedThreadFactory("Es"));
      //  this.esClient = new EsClient(clientConfig);
        this.executor.scheduleAtFixedRate(this::forceFlush, 0L, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void handle(InvokeContext invocation) {
        appendToQueue(invocation);
        maybeFlush();
    }

    @Override
    public void close() {
        this.executor.shutdown();
        //this.esClient.close();

    }

    private void appendToQueue(InvokeContext invocation) {
        try {
            queue.put(invocation);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void maybeFlush() {
        if (queue.size() >= CRITICAL_NUM) {
            forceFlush();
        }
    }

    private void forceFlush() {
        int capacity = Math.min(4096, queue.size());
        List<InvokeContext> invocations = new ArrayList<>(capacity);
        queue.drainTo(invocations, capacity);
        this.executor.submit(new EsTask(invocations));
    }

}
