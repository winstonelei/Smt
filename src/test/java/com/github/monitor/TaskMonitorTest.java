package com.github.monitor;

import java.util.UUID;

/**
 * Created by winstone on 2017/6/2.
 */
public class TaskMonitorTest {


    public static  void main(String[] args){

        String task = UUID.randomUUID().toString();
        TaskMonitor.addTask(task);
    }

}
