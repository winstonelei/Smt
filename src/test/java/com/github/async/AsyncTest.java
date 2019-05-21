package com.github.async;

import com.github.asyncLog.AppConfiguration;
import com.github.asyncLog.AsyncMsgLogger;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class AsyncTest {

    @Test
    public void testAsync()throws Exception{

        long start = 0, end = 0;
        start = System.currentTimeMillis();

        CostTimeCalculate calculate = new CostTimeCalculateImpl();
        AsyncInvoker invoker = new AsyncInvoker();

        for(int i=0;i<100;i++){
            CostTime elapse0 = invoker.submit(new AsyncCallback<CostTime>() {
                @Override
                public CostTime call() {
                    return calculate.calculate();
                }
            });

            CostTime elapse1 = invoker.submit(new AsyncCallback<CostTime>() {
                @Override
                public CostTime call() {
                    return calculate.calculate();
                }
            });

            CostTime elapse2 = invoker.submit(new AsyncCallback<CostTime>() {
                @Override
                public CostTime call() {
                    return calculate.calculate();
                }
            });
            System.out.println(i+ " async  call:[" + "result:" + elapse0 + ", status:[" + ((AsyncCallObject) elapse0)._getStatus() + "]");
            System.out.println(i+ " async  call:[" + "result:" + elapse1 + ", status:[" + ((AsyncCallObject) elapse1)._getStatus() + "]");
            System.out.println(i+ " async  call:[" + "result:" + elapse2 + ", status:[" + ((AsyncCallObject) elapse2)._getStatus() + "]");
        }

        end = System.currentTimeMillis();

        System.out.println(" async calculate time:" + (end - start));
    }
}
