package com.github.sshClient;

import java.io.IOException;

/**
 * Created by winstone on 2016/12/6.
 */
public class TestCliCommand {

    public static void main(String[] args){
        CliCommandExecutor executor = new CliCommandExecutor();
//        executor.setRunAtRemote("192.168.130.14",22,"root","zyuc@2017");
        try {
            //bin/kafka-topics.sh --create --zookeeper 114.55.253.15:2181,114.55.132.143:2181,114.55.252.185:218 --replication-factor 3 --partitions 3 --topic group_32
 /*           long createTime = System.currentTimeMillis();
            executor.copyFile("F:\\tmp\\lamp.jar","/usr/local/");
            long endTime = System.currentTimeMillis();
            System.out.println("haoshi="+(endTime - createTime));*/

            Pair<Integer, String> pair = executor.execute("perl D:\\tmp\\a.perl");
            System.out.println(pair.getValue());
          /*  System.out.println(pair.getKey());
            System.out.println("--------------------");
            System.out.println(pair.getValue());
            System.out.println("--------------------");
            System.out.println(pair.getFirst());
            System.out.println("--------------------");
            System.out.println(pair.getSecond());*/
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
