
package com.github.filequeue.log;

import com.github.filequeue.util.MappedByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 创建文件工具类
 */
public class FileRunner implements Runnable {
    private final Logger log = LoggerFactory.getLogger(FileRunner.class);
    // 删除队列
    private static final Queue<String> deleteQueue = new ConcurrentLinkedQueue<String>();
    // 新创建队列
    private static final Queue<String> createQueue = new ConcurrentLinkedQueue<String>();
    private String baseDir = null;
    private int fileLimitLength = 0;
    private volatile boolean keepRunning = true;

    public static void addDeleteFile(String path) {
        deleteQueue.add(path);
    }

    public static void addCreateFile(String path) {
        createQueue.add(path);
    }

    public FileRunner(String baseDir, int fileLimitLength) {
        this.baseDir = baseDir;
        this.fileLimitLength = fileLimitLength;
    }

    @Override
    public void run() {
        String filePath, fileNum;
        while (keepRunning) {
            filePath = deleteQueue.poll();
            fileNum = createQueue.poll();
            if (filePath == null && fileNum == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                continue;
            }
            if (filePath != null) {
                File delFile = new File(filePath);
                delFile.delete();
            }

            if (fileNum != null) {
                filePath = baseDir + fileNum + ".idb";
                try {
                    create(filePath);
                } catch (IOException e) {
                    log.error("预创建数据文件失败", e);
                }
            }
        }

    }

    private boolean create(String path) throws IOException {
        File file = new File(path);
        if (file.exists() == false) {
            if (file.createNewFile() == false) {
                return false;
            }
            RandomAccessFile raFile = new RandomAccessFile(file, "rwd");
            FileChannel fc = raFile.getChannel();
            MappedByteBuffer mappedByteBuffer = fc.map(MapMode.READ_WRITE, 0, this.fileLimitLength);
            mappedByteBuffer.put(LogEntity.MAGIC.getBytes());
            mappedByteBuffer.putInt(1);// 8 version
            mappedByteBuffer.putInt(-1);// 12next fileindex
            mappedByteBuffer.putInt(-2);// 16
            mappedByteBuffer.force();
            MappedByteBufferUtil.clean(mappedByteBuffer);
            fc.close();
            raFile.close();
            return true;
        } else {
            return false;
        }
    }
    
    public void exit(){
        keepRunning = false;
    }
}
