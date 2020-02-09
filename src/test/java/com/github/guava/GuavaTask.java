package com.github.guava;


import java.util.concurrent.Callable;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class GuavaTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        //Thread.sleep(100);
        System.out.println("thread 异步线程执行");
        return "wocao";
    }
}
