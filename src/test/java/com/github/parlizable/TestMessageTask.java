package com.github.parlizable;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestMessageTask {
    public static void main(String[] args) {
/*        SendMessageCache sendMesageCache = SendMessageCache.getInstance();
        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
        for(int i=0;i<=105 ;i++){
            Message message = new Message();
            message.setMessage(i +" test Message");
            queue.offer(message);
        }
        sendMesageCache.commit(queue);*/
        //实现多线程分布式完成任务，首先需要查询所有记录数据，然后得到实现计算的线程数，计算计算每个线程需要处理的数据数
        //最后一个线程需要实现补数操作
        Integer allCount = 1003;
        Integer threadCount = 10;
        Integer blocks = allCount / threadCount;
        for(int i=1;i<= threadCount ;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("begin",(i-1)*blocks+1);
            if(i == threadCount){
                map.put("end",i*blocks + allCount%threadCount);
            }else {
                map.put("end",i*blocks);
            }
            System.out.println("begin ="+map.get("begin"));
            System.out.println("end ="+map.get("end"));
        }



    }
}
