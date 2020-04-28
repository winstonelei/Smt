package com.github.rule;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class RuleTest {
    public static void main(String[] args) {
        try {
            ExpressRunner runner = new ExpressRunner();
            DefaultContext<String, Object> context = new DefaultContext<String, Object>();
            context.put("a",1);
            context.put("b",2);
            context.put("c",3);
            String express = "a+b*c";
            Object r = runner.execute(express, context, null, true, false);

            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
