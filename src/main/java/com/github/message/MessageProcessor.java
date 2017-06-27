package com.github.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by winstone on 2017/6/5.
 * 使用priorityBlockingQueue实现 有界优先对列功能
 */
public class MessageProcessor implements Closeable{

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    private static final long RETRY_PERIOD_UNIT = 15 * 1000;

    private final PriorityBlockingQueue<PriorityTask> taskQueue = new PriorityBlockingQueue<>(1000);

    private ExecutorService executor;

    private AtomicBoolean closed = new AtomicBoolean(false);

    private static  MessageProcessor messageProcessor = new MessageProcessor();

    public  static  MessageProcessor getInstance(){
        return messageProcessor;
    }

    private MessageProcessor(){
       executor = Executors.newFixedThreadPool(3, new StandardThreadExecutor.StandardThreadFactory("MessageProcessor"));
       executor.submit(new Runnable() {
               @Override
               public void run() {
                while(!closed.get()){
                    try{
                        PriorityTask task = taskQueue.take();

                        if(task.getMessage() == null){
                            break;
                        }

                        if(task.getNextFireTime() - System.currentTimeMillis() > 0){
                            TimeUnit.MICROSECONDS.sleep(1000);
                            taskQueue.put(task);
                            continue;
                        }

                        task.run();
                    }catch(Exception e){
                        logger.error(e.getMessage());
                    }
                  }
               }
           }
       );
    }


    public void submit(DefaultMessage message,MessageHandler handler){
        int taskCount;
        if((taskCount = taskQueue.size())>1000){
            logger.error("message processsor queue task count over "+taskCount);
        }
        taskQueue.add(new PriorityTask(message,handler));
    }


    @Override
    public void close() throws IOException {
        closed.set(true);
        taskQueue.add(new PriorityTask(null,null));
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        executor.shutdown();
    }

   class PriorityTask implements Runnable,Comparable<PriorityTask>{

       private DefaultMessage message;

       private MessageHandler handler;

       private long nextFireTime;

       private int retryCount;

       public PriorityTask(DefaultMessage message,MessageHandler messageHandler){

           this(message,messageHandler,System.currentTimeMillis()+RETRY_PERIOD_UNIT);

       }

       public PriorityTask(DefaultMessage message,MessageHandler messageHandler,long nextFireTime){
            super();
            this.message = message;
            this.handler = messageHandler;
            this.nextFireTime = nextFireTime;
       }


       @Override
       public void run() {
           try {
               logger.debug("begin re-process message:"+message.getMsgId());
               handler.process(message);
           } catch (Exception e) {
               retryCount++;
               logger.warn("retry[{}] mssageId[{}] error",retryCount,message.getMsgId());
               retry();
           }
       }


       private void retry(){
           if(retryCount == 3){
               logger.warn("retry_skip mssageId[{}] retry over {} time error ,skip!!!");
               return;
           }
           nextFireTime = nextFireTime + retryCount * RETRY_PERIOD_UNIT;
           //重新放入任务队列
           taskQueue.add(this);
           logger.debug("re-submit mssageId[{}] task to queue,next fireTime:{}",this.message.getMsgId(),nextFireTime);
       }


       @Override
       public int compareTo(PriorityTask o) {
           return (int) (this.nextFireTime - o.nextFireTime);
       }


       @Override
       public String toString() {
           return "PriorityTask [message=" + message.getMsgId() + ", messageHandler=" + handler.getClass().getSimpleName() + ", retryCount="
                   + retryCount + ", nextFireTime=" + nextFireTime + "]";
       }


       public DefaultMessage getMessage() {
           return message;
       }

       public void setMessage(DefaultMessage message) {
           this.message = message;
       }

       public long getNextFireTime() {
           return nextFireTime;
       }

       public void setNextFireTime(long nextFireTime) {
           this.nextFireTime = nextFireTime;
       }

   }
}
