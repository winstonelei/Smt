package com.github.threadRead;

import com.github.thread.masterworker.Master;
import com.github.thread.masterworker.Task;
import com.github.thread.masterworker.Worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2017/11/24 0024.
 */
public class ReadMsgThread  implements  Runnable {

    @Override
    public void run() {
        File file = new File("F:\\var\\log\\a.txt");
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Master master = new Master(new Worker(), 20);
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            AtomicInteger ai = new AtomicInteger();
            master.execute();
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                ai.incrementAndGet();
                Task t = new Task();
                t.setId(ai.get());
                t.setPrice(ai.get());
                t.setMessage(s);
                master.submit(t);
            }
            long entTime = System.currentTimeMillis();
            System.out.println("总耗时"+(entTime-startTime));
            System.out.println("总行数"+ai.get());
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
