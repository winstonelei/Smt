package com.com.github.scheduler;

import com.github.thread.multithreadRead.BigFileReader;
import com.github.thread.multithreadRead.IHandle;

/**
 * Created by winstone on 2017/8/16.
 */
public class BigFileReaderTest {


    public static void main(String[] args) {
        BigFileReader.Builder builder = new BigFileReader.Builder("F:\\tmp\\cursor.txt",new IHandle() {
            @Override
            public void handle(String line) {
                System.out.println(line);
            }
        });
        builder.withTreahdSize(10)
                .withCharset("gbk")
                .withBufferSize(1024*1024);
        BigFileReader bigFileReader = builder.build();
        bigFileReader.start();

    }

}
