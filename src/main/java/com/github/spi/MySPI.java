package com.github.spi;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MySPI {
    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";

}
