package com.github.limit;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRateLimit {

    //配置的某个key
    String confKey() default "";


    /**
     * 速率
     * @return
     */
    int replenishRate() default 100;

    /**
     * 容积
     * @return
     */
    int burstCapacity() default 1000;

}
