package com.github.scriptHandler;

import com.github.scriptyHandler.JobFileAppender;
import com.github.scriptyHandler.JobLogger;
import com.github.scriptyHandler.ScriptUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ScriptTest {

    @Test
    public void testWriteLog(){
        try {
            String logFileName = JobFileAppender.makeLogFileName(new Date(), 199);
            JobFileAppender.contextHolder.set(logFileName);

            JobLogger.log("<br>----------- job execute start -----------<br>----------- Param:" + "hello script");
            // cmd
            String cmd = "python";
            String jobId = "111";
            String gluesource = "adfaf";
            String glueUpdatetime = "20190614";

            // make script file
            String scriptFileName = JobFileAppender.getGlueSrcPath()
                    .concat(File.separator)
                    .concat(String.valueOf(jobId))
                    .concat("_")
                    .concat(String.valueOf(glueUpdatetime))
                    .concat(".py")
                    ;
            File scriptFile = new File(scriptFileName);
            if (!scriptFile.exists()) {
                ScriptUtil.markScriptFile(scriptFileName, gluesource);
            }

            // log file,通过threadlocal 获取，此threadLocal 支持父子线程
            logFileName = JobFileAppender.contextHolder.get();

            String[] scriptParams = new String[3];
            scriptParams[0] = "a";

            // invoke
            JobLogger.log("----------- script file:"+ scriptFileName +" -----------");
            int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);

            if (exitValue == 0) {
                System.out.println("succes");
            } else {
                System.out.println("eroor");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
