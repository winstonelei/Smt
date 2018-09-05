package com.github.thread.treadTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DeadLock2 {

    public static  final Lock lock1 = new ReentrantLock();

    public static final Lock lock2 = new ReentrantLock();

    static class RunTask1 implements Runnable{
        private int id=-1;
        public RunTask1(int i){
            id = i;
        }
        @Override
        public void run() {
           while(true){
               lock1.lock();
               lock2.lock();
               System.out.println(id);
               lock2.unlock();
               lock1.unlock();
           }
        }
    }

    static class RunTak2 implements  Runnable{
        private int id =-2;
        public RunTak2(int i){
            id=i;
        }
        @Override
        public void run() {
            while(true){
                lock2.lock();
                lock1.lock();
                System.out.println(id);
                lock1.unlock();
                lock2.unlock();
            }
        }
    }



    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();

        service.execute(new RunTask1(1));

        service.execute(new RunTak2(2));

    }

}
