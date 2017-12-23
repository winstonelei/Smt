package com.github.parlizable;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SendMessageCache extends  MessageCache<Message> {

    private SendMessageCache(){}

    private static SendMessageCache sendMessageCache;

    public synchronized static SendMessageCache getInstance(){

        if(null == sendMessageCache){
            sendMessageCache = new SendMessageCache();
        }

        return sendMessageCache;
    }


    @Override
    protected void parallelDispatch(LinkedList<Message> list) {
      List<Callable<Void>> tasks = new ArrayList<>();
      int startPosition = 0;
      Pair<Integer,Integer> pair = calculateBlocks(10,list.size());
      int numberOfThreads = pair.getRight();
      int blocks = pair.getLeft();

      for(int i=0;i<numberOfThreads;i++){
          Message[] task = new Message[blocks];
          System.arraycopy(list.toArray(), startPosition, task, 0, blocks);
          tasks.add(new SendMessageTask(task));
          startPosition += blocks;
      }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        for(Callable<Void> callable : tasks){
            executor.submit(callable);
        }


    }
}
