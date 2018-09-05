package com.github.thread.treadTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 *
 * 上面的代码开启了两个线程，两个线程都循环打印字符串，正常来说两个线程都会不断打印，但这里出现了死锁，导致两个线程都会打印停止，
 * 逻辑分析：两个线程执行到某一时刻，线程一执行到synchronized (lock2){这一句，获得了对象lock2上的锁，线程二执行到synchronized (lock1){这一句，
 * 获得了对象lock1上的锁，接下来，线程一需要执行synchronized (lock1){这一句尝试获取lock1的锁，由于lock1的锁被线程二占用，线程一等待线程二释放lock1的锁，
 * 这时线程二需要执行synchronized (lock2){这一句，尝试获取lock2上的锁，
 * 由于lock2上的锁被线程一占用，线程二等待线程一释放lock2上的锁，这样，两个线程互相等待释放锁，导致两个线程永远无法执行后面的代码，导致死锁；
 */
public class DeadLock {

    public static void main(String[] args) {
      Object obj1 = new Object();
      Object obj2 = new Object();
      ExecutorService service = Executors.newCachedThreadPool();

      service.execute(()->{
          while(true){
              synchronized (obj1){
                synchronized (obj2){
                    System.out.println("Thread 1");
                }
              }
          }
      });

      service.execute(()->{
          while (true) {
             synchronized (obj2){
                synchronized (obj1){
                    System.out.println("Thread 2");
                }
             }
          }
      });


      service.shutdown();



    }

}
