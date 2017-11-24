package com.github.threadRead;

import com.github.message.DefaultMessage;
import com.github.message.MessageHandler;
import com.github.message.MessageProcessor;
import com.github.message.TestMessageHandler;
import com.github.thread.masterworker.Master;
import com.github.thread.masterworker.Task;
import com.github.thread.masterworker.Worker;
import net.sf.ehcache.util.NamedThreadFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2017/11/24 0024.
 */
public class ReadMsgSecond {
    public static String txt2String(File file){
        long startTime = System.currentTimeMillis();
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
        return result.toString();
    }
    public static void main(String[] args) {
       /* File file = new File("F:\\var\\log\\a.txt");
       txt2String(file);*/
        ExecutorService executorService =
                Executors.newSingleThreadExecutor(new NamedThreadFactory("local.register.watch.executor"));

        ReadMsgThread readMsgThread = new ReadMsgThread();
        executorService.execute(new Thread(readMsgThread));

    }
}
