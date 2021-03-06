package com.github.batch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BatchUtilsTest {
    private static final int BATCH_SIZE = 9;

    @Test
    public void testBatch(){
      /*  for(int i=0;i<10;i++){
            List<String> list = new ArrayList();
            list.add("abc");
            list.add("def");
            list.add("hgc");
            list.add("eee");
            list.add("aaa");
            Random r = new Random();
            int n2 = r.nextInt(5);
            System.out.println(list.get(n2));
        }*/


       // int index = list.get()

        List<Job> jobs = new ArrayList<>();
        for(int i=0;i<19;i++){
            Job job = new Job();
            job.setId(i);
            job.setName("name"+i);
            jobs.add(job);
        }
        int size = jobs.size();
        System.out.println(size/BATCH_SIZE);
        BatchUtils.batchExecute(size, BATCH_SIZE, jobs, new BatchUtils.Executor<Job>() {
            @Override
            public boolean execute(List<Job> list) {
                System.out.println(list.size());
                list.stream().forEach(item-> System.out.println(item));
                return true;
            }
        });

    }
}
