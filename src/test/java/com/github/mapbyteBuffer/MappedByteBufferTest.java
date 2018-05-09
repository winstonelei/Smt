package com.github.mapbyteBuffer;


import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by winstone on 2017/4/11.
 */
public class MappedByteBufferTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try{
            RandomAccessFile rafi = new RandomAccessFile("D:\\soft\\kafka_2.11-1.1.0.tgz", "r");
            RandomAccessFile rafo = new RandomAccessFile("D:\\soft\\kafka.tgz", "rw");
            FileChannel fci = rafi.getChannel();
            FileChannel fco = rafo.getChannel();
            long size = fci.size();
            MappedByteBuffer mbbi = fci.map(FileChannel.MapMode.READ_ONLY, 0, size);
            MappedByteBuffer mbbo = fco.map(FileChannel.MapMode.READ_WRITE, 0, size);
            long start = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                byte b = mbbi.get(i);
                mbbo.put(i, b);
            }
            fci.close();
            fco.close();
            rafi.close();
            rafo.close();
            System.out.println("Spend: "+(double)(System.currentTimeMillis()-start) / 1000 + "s");
        }catch(Exception e){
            e.printStackTrace();
        }

    /*  try {
          int DATA_CHUNK = 53*1024 * 1024;
          MappedFile rafo = new MappedFile("fc1.dat", "D:\\temp\\");
          long start = System.currentTimeMillis();
          byte[] data = new byte[DATA_CHUNK];
          rafo.appendData( data);
          System.out.println("Spend: "+(double)(System.currentTimeMillis()-start) / 1000 + "s");

      }catch (Exception e){
          e.printStackTrace();
      }*/



    }
}
