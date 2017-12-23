package com.github.parlizable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SendMessageTask implements Callable<Void> {

    private Message[] messages;

    public SendMessageTask(Message[] tasks){
        messages = tasks;
    }
    @Override
    public Void call() throws Exception {
        if(ArrayUtils.isNotEmpty(messages)){
          for(Message messgae : messages){
              System.out.println(messgae.message);
          }
        }
        return null;
    }
}
