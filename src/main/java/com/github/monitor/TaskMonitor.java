package com.github.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2017/6/2.
 */
public class TaskMonitor {

    private static final Log logger = LogFactory.getLog(TaskMonitor.class);

    private BlockingQueue<String> queue = new LinkedBlockingDeque<>();

    private static TaskMonitor instance = null;

    private TaskMonitor(){}

    public static TaskMonitor getInstance(){
        if(null == instance){
            synchronized(TaskMonitor.class){
                instance = new TaskMonitor();
                Thread thread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                TaskMonitor.instance.runTask();
                            }
                        }
                );

                thread.start();
            }

        }
        return instance;
    }

    /**
     * 构造执行队列
     */
    private void runTask(){

        try{
            while (true){
                String task = this.queue.poll(5L,TimeUnit.MILLISECONDS); //.take();//使用take方式会阻塞队列，所以建议使用poll，当服务停止的时候服务也会停止
                if(null != task){
                    logger.info("接受到的任务="+task);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    /**
     * 任务队列中添加任务
     * @param task
     */
    public static void addTask(String task){

        try{
            TaskMonitor sendDataMonitor = getInstance();
            logger.info("得到的任务实例"+sendDataMonitor.hashCode());
            sendDataMonitor.queue.offer(task); //.put(task);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }
}
