package com.github.guava;

import com.github.guavaFuture.RcfConstants;
import com.github.guavaFuture.RcfThreadPool;
import com.google.common.util.concurrent.*;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class GuavaListenerTest {

    private static volatile ListeningExecutorService threadPoolExector;
    private static int threadNums = RcfConstants.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;
    private static int queueNums = RcfConstants.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    public static void main(String[] args) {
        if(threadPoolExector == null){
            synchronized (GuavaListenerTest.class){
                if(threadPoolExector == null){
                    threadPoolExector = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RcfThreadPool.getExecutorWithJmx(threadNums,queueNums));
                }
            }
        }
        GuavaTask task = new GuavaTask();

       ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());


     /*   ListenableFuture<String> listenableFuture = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "xxx";
            }
        });*/
        ListenableFuture<String> listenableFuture = service.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            public void onSuccess(String result) {
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("异步执行完成");
                System.out.println("返回结果"+result);

                service.shutdown();
            }

            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExector);




    }
}
