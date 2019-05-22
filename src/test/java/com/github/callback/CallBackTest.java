package com.github.callback;

import com.github.async.*;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *   使用callback回调模式，发送方一定 发送完毕消息后，等到结果集返回后触发调用才可以
 *   原理是使用condition 的wait  或者是signal 方法实现结果的通知
 *   也可以使用countdownlatch 实现这种功能
 * @author wangl
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CallBackTest {
    //保存消息messageId 和 callback的映射关系
    private static ConcurrentHashMap<String, MessageCallBack> mapCallBack = new ConcurrentHashMap<String, MessageCallBack>();

    @Test
    public void testCallBack()throws Exception{
        CallBackTest callBackTest = new CallBackTest();
        MessageRequest request = new MessageRequest();
        request.setMessageId("123");
        request.setClassName("test");
        request.setMethodName("test");
        MessageCallBack callBack = callBackTest.sendRequest(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
            try {
               //模拟服务器 返回结果集，然后通知消息
                Thread.sleep(1000);
                System.out.println("接收消息");
                MessageResponse response = new MessageResponse();
                response.setMessageId("123");
                response.setResult("res1323");
                response.setError("");
                response.setReturnNotNull(false);
                String messageId = response.getMessageId();
                Thread.sleep(500);
                MessageCallBack callBack = mapCallBack.get(messageId);
                if (callBack != null) {
                    mapCallBack.remove(messageId);
                    callBack.over(response);
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("发送消息");
        System.out.println(callBack.start());
    }

    public MessageCallBack sendRequest(MessageRequest request) {
        MessageCallBack callBack = new MessageCallBack(request);
        mapCallBack.put(request.getMessageId(), callBack);
        //channel.writeAndFlush(request); 发送request到服务端
        return callBack;
    }
}
