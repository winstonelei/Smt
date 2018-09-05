package com.github.thread.queue;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DelayedQueueDemo {
    private static final AtomicLong taskSequencer  = new AtomicLong(0);

    static  class DelayedTask implements Delayed{

        private long runTime;
        private long sequence;
        private Runnable task;

        public DelayedTask(int dealayedSeconds,Runnable task){
            this.runTime = System.currentTimeMillis() +dealayedSeconds*1000;
            this.sequence = taskSequencer.getAndIncrement();
            this.task = task;
        }

        public Runnable getTask(){
            return  task;
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            return unit.convert(runTime-System.currentTimeMillis(),TimeUnit.MICROSECONDS);
        }

        @Override
        public int compareTo(@NotNull Delayed o) {
            if (o == this) {
                return 0;
            }
            if (o instanceof DelayedTask) {
                DelayedTask other = (DelayedTask) o;
                if (runTime < other.runTime) {
                    return -1;
                } else if (runTime > other.runTime) {
                    return 1;
                } else if (sequence < other.sequence) {
                    return -1;
                } else {
                    return 1;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        try {
            DelayQueue<DelayedTask> delayQueue = new DelayQueue<DelayedTask>();

            delayQueue.put(new DelayedTask(1, new Runnable() {
                @Override
                public void run() {
                    System.out.println("first exeucte task");
                }
            }));

            delayQueue.put(new DelayedTask(2, new Runnable() {
                @Override
                public void run() {
                    System.out.println("second exeucte task");
                }
            }));

            while (delayQueue.size()>0){
                DelayedTask delayedTask = delayQueue.take();
                delayedTask.getTask().run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
