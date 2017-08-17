package com.github.monitor;

import java.util.UUID;

/**
 * Created by winstone on 2017/6/2.
 */
public class TaskMonitorTest {


    public static  void main(String[] args){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                           try{
                               String task = UUID.randomUUID().toString();
                               TaskMonitor.addTask(task);
                               Thread.sleep(1000L);
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                        }
                    }
                }
        ).start();
    }
}
