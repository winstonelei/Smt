package com.github.parlizableSplitTask;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessageSplitTask implements Callable<List<Integer>> {

    private List<Integer> list;
    private CountDownLatch endLatch;

    public MessageSplitTask(List<Integer> input,CountDownLatch inputCountLatch){
        this.list = input;
        this.endLatch = inputCountLatch;
    }

    @Override
    public List<Integer> call() throws Exception {
        if(CollectionUtils.isNotEmpty(list)){
            System.out.println("MessageSplitTask   " + Thread.currentThread().getName()+list);
        }
        Thread.sleep(1000);
        endLatch.countDown();
        return list;
    }
}
