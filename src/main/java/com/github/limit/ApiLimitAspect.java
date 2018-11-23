/*
package com.github.limit;

import org.aspectj.lang.annotation.Around;
import org.mp4parser.aspectj.lang.ProceedingJoinPoint;
import org.mp4parser.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

*/
/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 *//*

@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ApiLimitAspect {

    public static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<String, Semaphore>();

    @Around("execution(* com.xx.*.*.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint){
        Object result = null;
        Semaphore semap = null;
        Class<?> clazz = joinPoint.getTarget().getClass();
        String key = this.getRateLimitKey(clazz,joinPoint.getSignature().getName());
        if(null != key){
            semap = semaphoreMap.get(key);
        }else{
            semap = semaphoreMap.get("open.api.defaultLimit");
        }
        try {
            if(null != semap){
                semap.acquire();
            }
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException();
        } finally {
            if(semap != null ){
                semap.release();
            }
        }
        return result;

    }

    private String getRateLimitKey(Class<?> clazz,String methodName){
        for(Method method : clazz.getMethods()){
            if(method.getName().equals(methodName)){
                if(method.isAnnotationPresent(ApiRateLimit.class)){
                    String key = method.getAnnotation(ApiRateLimit.class).confKey();
                    return key;
                }
            }
        }
        return null;
    }

}
*/
