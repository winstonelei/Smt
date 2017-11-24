package com.github.threadRead;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2017/11/24 0024.
 */
public class WriteMsg {

    public static void main(String[] args)  {

        try{
            File f = new File("F:\\var\\log\\a.txt");
            if(!f.exists()){
                f.createNewFile();
            }
            AtomicInteger atomicInteger = new AtomicInteger();
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            for(int i=0;i<=100000;i++){
                if(i%2==0){
                    out.write(i +" 写入文件，lisi\r\n"); // \r\n即为换行
                }else{
                    atomicInteger.incrementAndGet();
                    out.write(i +" 写入文件，zhangsan\r\n"); // \r\n即为换行
                }
            }

            System.out.println(atomicInteger.get());
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
