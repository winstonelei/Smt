package com.github.ayncLog;

import com.github.asyncLog.AppConfiguration;
import com.github.asyncLog.AsyncMsgLogger;
import org.apache.logging.log4j.core.async.AsyncLogger;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class AsyncLoggerTest {

    @Test
    public void testWriteLog()throws Exception{

        AppConfiguration appConfiguration = new AppConfiguration();
        appConfiguration.setUserName("winstone");
        appConfiguration.setMsgLoggerPath("D:\\tmp\\");
        AsyncMsgLogger asyncMsgLogger = new AsyncMsgLogger(appConfiguration);
        for(int i=0;i<100;i++){
            asyncMsgLogger.log("ceshi"+i+"log");
        }

        Thread.sleep(100000);
    }
}
