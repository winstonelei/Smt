package com.github.threadRead;

import com.github.message.DefaultMessage;
import com.github.message.MessageHandler;
import com.github.message.MessageProcessor;
import com.github.message.TestMessageHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2017/11/24 0024.
 */
public class ReadMsg {
    public static String txt2String(File file){
        long startTime = System.currentTimeMillis();
        StringBuilder result = new StringBuilder();
        MessageProcessor processor = MessageProcessor.getInstance();
        MessageHandler handler = new TestMessageHandler();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            AtomicInteger ai = new AtomicInteger();
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                ai.incrementAndGet();
                processor.submit(new DefaultMessage(s), handler);
           //     Thread.sleep(RandomUtils.nextLong(100, 500));
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
        File file = new File("F:\\var\\log\\a.txt");
       txt2String(file);

    }
}
