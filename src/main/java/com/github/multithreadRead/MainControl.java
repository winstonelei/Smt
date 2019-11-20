package com.github.multithreadRead;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 大文件切分解析，写入数据库主控制类
 *
 * @author winstone
 * @date 2019/4/29 下午7:50
 */
public class MainControl {

    public static void main(String args[]) throws Exception{

        String filepath = "D:\\slview\\nms\\data\\uddata\\bak\\log1571195655796.txt";
        final RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filepath), "r");

        //分区, 跳过标题，从第二行开始切分
        String title = "time,src_ip,request_url,dest_ip,dest_port,method,user_agent,connection,server,status,protocol\r\n";
        int partionCount = 5;
        long startPosition = 0;//title.length();
        long totalSize = randomAccessFile.length();
        List<FilePartion> filePartionList = FilePartioner.partion(randomAccessFile, startPosition, partionCount, totalSize);

        //多线程执行文件分区解析和处理任务
        final ExecutorService executor = Executors.newFixedThreadPool(partionCount);
        final Long start = System.currentTimeMillis();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(filePartionList.size(), new Runnable() {
            @Override
            public void run() {
                long end = System.currentTimeMillis();
                executor.shutdown();
                try {
                    randomAccessFile.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + " all work is done ,cost:"
                        + (end - start) / 1000 + ("(s)"));
            }
        });

        for (FilePartion filePartion : filePartionList) {
            executor.submit(new FileParseTask(randomAccessFile, filePartion, cyclicBarrier));
        }
    }
}
