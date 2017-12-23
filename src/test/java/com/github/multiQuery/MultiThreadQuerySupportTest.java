package com.github.multiQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MultiThreadQuerySupportTest {

    public static void main(String[] args) {

        MyMultiQuery multiQuery = new MyMultiQuery();
        multiQuery.setMaxThreadsPerQuery(3);
        multiQuery.setQueryTimeout(5000);
        try {
            List<Person> list = new ArrayList<>();
            for(int i=0;i<=5;i++){
                Person p = new Person();
                p.setAge(10+i);
                p.setName("tt");
                list.add(p);
            }
            List<Boolean> resultList =  multiQuery.query(list);
            System.out.println(resultList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            multiQuery.shutdown();
        }
    }
}

class MyMultiQuery extends MultiThreadQuerySupport<Person,Boolean>{
    @Override
    protected Boolean queryOne(Person person) {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
