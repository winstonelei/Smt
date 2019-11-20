package com.github.multithreadRead;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件分区器，将大文件进行分区，便于并行处理
 * 注意，所谓分区，是通过读文件，确定分区的索引范围，并未加载数据到内存
 * 分区对象不包含数据本身
 * @author winston
 * @date 2019/4/28 下午7:12
 */
public class FilePartioner {

    /**
     * 对文件进行分区
     * 根据分区数量和文件总大小，可计算每个分区的大小，按照分区大小进行切割
     * @param randomAccessFile 待分区文件对象
     * @param start 分区的全局其实位置
     * @param splitCount 分区数量
     * @param totalSize 文件总大小
     * @return
     */
    public static List<FilePartion> partion(RandomAccessFile randomAccessFile, long start, int splitCount, long totalSize) throws Exception{

        if (splitCount < 2) {
            throw new RuntimeException("分区数量至少2个");
        }

        Long partionLength = totalSize / splitCount;

        //初始化
        List<FilePartion> filePartionList = new ArrayList<>(splitCount);
        for (int i = 0; i < splitCount; i++) {
            FilePartion filePartion = new FilePartion();
            filePartion.setStart(start + i * partionLength);
            filePartion.setEnd(filePartion.getStart() + partionLength - 1);
            filePartionList.add(filePartion);
        }

        //修正分区块，均匀 -> 不均匀， 确保每个分区都在\r\n处结束
        long index = filePartionList.get(0).getEnd();
        for (int i = 1; i < splitCount; i++) {

            //如果不是\r\n，则继续往下探测, 直到出现第一个\r
            randomAccessFile.seek(index);
            byte oneByte = randomAccessFile.readByte();
            while (oneByte != '\r' && oneByte != '\n') {
                byte[] tmp = new byte[1];
                tmp[0] = oneByte;
                randomAccessFile.seek(index++);
                oneByte = randomAccessFile.readByte();
            }

            //同时修正前后2个区块
            FilePartion previous = filePartionList.get(i - 1);
            FilePartion current = filePartionList.get(i);
            previous.setEnd(index + 1);
            current.setStart(index + 2);
            index = current.getEnd();
        }

        return filePartionList;
    }


}
