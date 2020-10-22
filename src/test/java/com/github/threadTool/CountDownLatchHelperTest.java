package com.github.threadTool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CountDownLatchHelperTest {

    @Test
    public void testExecute(){

        List<TestRunnable> list = new ArrayList<>();
        TestRunnable testRunnable = new TestRunnable();
        testRunnable.setName("test1");
        TestRunnable testRunnable1 = new TestRunnable();
        testRunnable1.setName("test2");
        list.add(testRunnable);
        list.add(testRunnable1);

        CountDownLatchHelper<Boolean> countDownLatchHelper = new CountDownLatchHelper<Boolean>();
        for(TestRunnable runnable : list){
            countDownLatchHelper.addExecute(new IExecute<Boolean>() {
                @Override
                public Boolean execute() {
                    return true;
                }
            });
        }

        List<Boolean> hasOks = countDownLatchHelper.execute().getData();

        System.out.println(hasOks);


    }


}
