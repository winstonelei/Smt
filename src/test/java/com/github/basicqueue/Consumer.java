package com.github.basicqueue;

import com.github.mappedByteBuffer.BasicQueue;

import java.io.IOException;
import java.util.Random;

public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        try {
            BasicQueue queue = new BasicQueue("D:\\temp\\", "task");
            Random rnd = new Random();
            while (true) {
                byte[] bytes = queue.dequeue();
                if (bytes == null) {
                    Thread.sleep(rnd.nextInt(1000));
                    continue;
                }
                System.out.println("consume: " + new String(bytes, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}