package com.github.parlizableSplitTask;

import com.github.guavaFuture.NamedThreadFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *  实现线程切分计算,多线程处理数据，结果返回
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestSplitTask {

    @Test
    public void testSplit()throws  Exception{
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<18300;i++){
            list.add(i);
        }
        System.out.println(list);
        //[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        //获取元素3-7
/*        List<Integer> subList2 = list.subList(0, 3);
        System.out.println(subList2);
        List<Integer> subList = list.subList(3, 8);
        System.out.println(subList);
        List<Integer> subList1 = list.subList(8, 10);
        System.out.println(subList1);*/

        //实现多线程分布式完成任务，首先需要查询所有记录数据，然后得到实现计算的线程数，计算计算每个线程需要处理的数据数
        //最后一个线程需要实现补数操作
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        //核心线程数量大小
        final int corePoolSize = 10;//Math.max(2, Math.min(CPU_COUNT - 1, 4));//
        //线程池最大容纳线程数
        final int maximumPoolSize = 20;//CPU_COUNT * 2 + 1;//
        System.out.println("corePoolSize="+corePoolSize);
        System.out.println("maximumPoolSize="+maximumPoolSize);
        //线程空闲后的存活时长
        final int keepAliveTime = 30;
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),   new NamedThreadFactory("Pool"));
        Integer allCount = list.size();
        Integer threadCount = 10;
        Integer blocks = allCount / threadCount;
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        List<Future<List<Integer>>> allResult = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for(int i=1;i<= threadCount ;i++){
            Map<String,Integer> map = new HashMap<>();
            map.put("begin",(i-1)*blocks+1);
            if(i == threadCount){
                map.put("end",i*blocks + allCount%threadCount);
            }else {
                map.put("end",i*blocks);
            }
            List<Integer> subList = list.subList(map.get("begin")-1,map.get("end"));
            MessageSplitTask splitTask = new MessageSplitTask(subList,endLatch);
            Future<List<Integer>> result = pool.submit(splitTask);
            allResult.add(result);
        }
        endLatch.await();

        for(Future future:allResult){
            List<Integer> futureRes = (List<Integer>) future.get(1000,TimeUnit.SECONDS);
            System.out.println("future RES "+futureRes);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("costTime="+(endTime-startTime));
        pool.shutdown();
    }


}
