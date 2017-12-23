package com.github.parlizable;

import com.sun.media.jfxmedia.logging.Logger;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public abstract class MessageCache<T> {

    private ConcurrentLinkedQueue<T> cacheQueue = new ConcurrentLinkedQueue<T>();

    private Semaphore semaphone = new Semaphore(0);


    public void appendMessage(T obj){
        cacheQueue.add(obj);
        semaphone.release();
    }


    public void commit(ConcurrentLinkedQueue<T> tasks) {
        commitMessage(tasks);
    }


    private void commitMessage(ConcurrentLinkedQueue<T> messages) {
        LinkedList<T> list = new LinkedList<T>();
        list.addAll(messages);
        cacheQueue.clear();
        if (list != null && list.size() > 0) {
            parallelDispatch(list);
            list.clear();
        }
    }

    protected Pair<Integer, Integer> calculateBlocks(int parallel, int sizeOfTasks) {
        int numberOfThreads = parallel > sizeOfTasks ? sizeOfTasks : parallel;
        Pair<Integer, Integer> pair = new MutablePair<Integer, Integer>(new Integer(sizeOfTasks / numberOfThreads), new Integer(numberOfThreads));
        return pair;
    }


    public boolean hold(long timeOut){
        try{
           return semaphone.tryAcquire(timeOut, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
            return false;
        }
    }

    protected  abstract void parallelDispatch(LinkedList<T> list);

}
