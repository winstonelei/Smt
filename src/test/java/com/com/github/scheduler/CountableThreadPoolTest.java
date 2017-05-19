package com.com.github.scheduler;

import com.github.countableThreadPool.CountableThreadPool;

import java.util.Random;

/**
 * Created by winstone on 2016/12/6.
 */
public class CountableThreadPoolTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Runnable r1=new ThreadTest();
        Runnable r2=new ThreadTest();
        Runnable r3=new ThreadTest();
        Runnable r4=new ThreadTest();

        CountableThreadPool pool =new CountableThreadPool(2);

        pool.execute(r1);

        pool.execute(r2);

        pool.execute(r3);

        pool.execute(r4);

        pool.shutdown();
    }

    static class ThreadTest implements Runnable{
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+new Random().nextInt(100));
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
