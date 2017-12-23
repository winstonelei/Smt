package com.github.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2016/12/6.
 *
 * 调度器相关
 *
 */
public class SchedulerTest {

    public static void main(String[] args){

        ExecutorService service = Executors.newCachedThreadPool();

        // final MemoryChannel memoryChannel = new MemoryChannel(10);
        final FileChannel memoryChannel = new FileChannel("F:\\tmp\\webmagic");
        final AtomicInteger ai = new AtomicInteger();

        //创建十个线程放入到调度队列中
        for(int i=0;i<20 ;i++){
            Runnable rn = new Runnable() {
                @Override
                public void run() {
     //               System.out.println(ai.get());
                    memoryChannel.offer(ai.incrementAndGet()+"test");
                }
            };
            service.execute(rn);
        }

        for(int j=0; j<10; j++){
            Runnable rn1 = new Runnable() {
                @Override
                public void run() {
                    System.out.println(memoryChannel.poll());
                }
            } ;
            service.execute(rn1);
        }

        service.shutdown();

    }

}
