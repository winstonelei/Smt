package com.github.threadTool;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CountDownThread <T> implements Runnable {

    private ExecutorService threadPool;

    public CountDownThread(ExecutorService threadPool, List<T> list, IExecute<T> execute, CountDownLatch currentThread) {
        this.threadPool = threadPool;
        this.list = list;
        this.execute = execute;
        this.currentThread = currentThread;
    }

    private CountDownLatch currentThread;

    private IExecute<T> execute;

    private List<T> list;

    @Override
    public void run() {
        list.add(execute.execute());
        currentThread.countDown();
    }

    public void execute() {
        threadPool.execute(this);
    }


}
