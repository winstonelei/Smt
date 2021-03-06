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

    /**
     * 10001
     * costTime=117
     * @param args
     */
    public static void main(String[] args) {
        MyMultiQuery multiQuery = new MyMultiQuery();
/*        multiQuery.setMaxThreadsPerQuery(3);
        multiQuery.setQueryTimeout(10000);*/

        try {
            for(int j=0;j<10;j++){
                List<Person> list = new ArrayList<>();
                for(int i=0;i<=1_0000;i++){
                    Person p = new Person();
                    p.setAge(10+i);
                    p.setName("tt");
                    list.add(p);
                }
                long start = System.currentTimeMillis();
                List<Person> resultList =  multiQuery.query(list);
        /*    for(Person person : resultList){
                System.out.println("new PersonName = " +person.getName());
            }*/
                System.out.println(resultList.size());
                long end = System.currentTimeMillis();
                System.out.println("第"+j+"次 costTime="+(end-start));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            multiQuery.shutdown();
        }
    }
}

class MyMultiQuery extends MultiThreadQuerySupport<Person,Person>{
    @Override
    protected Person queryOne(Person person) {
        try {
 /*           System.out.println(Thread.currentThread().getName());
            Thread.sleep(7000);*/

            person.setName("Hello" + person.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }
}
