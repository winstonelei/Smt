/*
package com.github.limit;

import com.github.jarscaner.ClasspathPackageScannerUtils;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
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

public class InitApiLimitRateListener  implements ApplicationListener<ApplicationPreparedEvent>{

    //controller path
    private String controllerPath;


    public InitApiLimitRateListener(String controllerPath){
        this.controllerPath = controllerPath;
    }

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {

        try {
            initLimitRateAPI();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void initLimitRateAPI() throws IOException, ClassNotFoundException {
        Object rate = System.getProperty("open.api.defaultLimit") == null ? 100 : System.getProperty("open.api.defaultLimit");
        ApiLimitAspect.semaphoreMap.put("open.api.defaultLimit", new Semaphore(Integer.parseInt(rate.toString())));
        ClasspathPackageScannerUtils scan = new ClasspathPackageScannerUtils(this.controllerPath);
        List<String> classList = scan.getFullyQualifiedClassNameList();
        for(String clazz : classList){
            Class<?> clz = Class.forName(clazz);
            if(!clz.isAnnotationPresent(RestController.class)){
                continue;
            }
            Method[] methods = clz.getMethods();
            for(Method method : methods){
                if(method.isAnnotationPresent(ApiRateLimit.class)){
                    String configKey = method.getAnnotation(ApiRateLimit.class).confKey();
                    if (System.getProperty(configKey) != null) {
                        int limit = Integer.parseInt(System.getProperty(configKey));
                        ApiLimitAspect.semaphoreMap.put(configKey, new Semaphore(limit));
                    }
                }
            }

        }
    }
}
*/
