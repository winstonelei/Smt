package com.github.multithreadRead;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CyclicBarrier;

/**
 * 文件解析任务，针对一个特定的分区
 * @author winston
 * @date 2019/4/28 下午7:38
 */
public class FileParseTask implements Runnable{


    /**
     * 文件访问类
     */
    private RandomAccessFile fileAccess;


    /**
     * 处理的文件切分信息
     */
    private FilePartion filePartion;

    /**
     * 线程协作控制类
     */
    private CyclicBarrier barrier;

    /**
     * 文件切分的大小
     */
    private long sliceSize;

    /**
     * 文件读取到内存中的缓冲区
     */
    private byte[] buffer ;

    /**
     * 缓冲区大小1M
     */
    private int bufferSize = 1024*1024;

    /**
     * 数据处理类
     */
    private DataHandler handler;


    public FileParseTask(RandomAccessFile fileAccess, FilePartion filePartion, CyclicBarrier barrier) {
        this.fileAccess = fileAccess;
        this.filePartion = filePartion;
        this.barrier = barrier;

        this.buffer = new byte[bufferSize];
        this.handler = new DataHandler();
        this.sliceSize = filePartion.getEnd() - filePartion.getStart() + 1;
    }

    /**
     * 文件分区解析
     * 1 将分区数据映射到内存镜像
     * 2 读取内存镜像数据到缓冲区
     * 3 逐行处理缓冲区数据，以\r\n为一行， 交给DataHandler进行处理
     * 4 处理完成后，通知栅栏
     */
    @Override
    public void run() {

        try {

            //1 文件映射到内存镜像
            MappedByteBuffer mappedByteBuffer = fileAccess.getChannel().map(FileChannel.MapMode.READ_ONLY, filePartion.getStart(), this.sliceSize);

            //2 循环读取数据到缓冲区，每次读取一个缓冲区的大小并逐行处理
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            for (int offset = 0; offset < sliceSize; offset += bufferSize) {

                //最后一次读取不一定是bufferSize，需要特殊处理
                long readLength = offset + bufferSize <= sliceSize ? bufferSize : sliceSize - offset;

                mappedByteBuffer.get(buffer, 0, (int)readLength);

                //逐行处理缓冲区的数据，用ByteArrayOutputStream将以流的方式读取buffer，遇到\r\n时说明满足一行，转成string交给DataHandler处理
                for (int i = 0; i < readLength; i++) {
                    byte tmp = buffer[i];
                    if (tmp == '\r') {
                        continue;
                    }else if (tmp == '\n') {
                        handler.handle(new String(byteArrayOutputStream.toByteArray()), false);
                        byteArrayOutputStream.reset();
                    }else {
                        byteArrayOutputStream.write(tmp);
                    }
                }
            }

            //如果流还有数据，则进行最后一行处理
            if (byteArrayOutputStream.size() > 0) {
                handler.handle(new String(byteArrayOutputStream.toByteArray()), true);
            }
            barrier.await();

        }catch (Exception e) {

            e.printStackTrace();
        }

    }
}
