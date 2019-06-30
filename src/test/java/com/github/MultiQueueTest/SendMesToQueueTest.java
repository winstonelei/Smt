package com.github.MultiQueueTest;

import com.github.messageFileStorage.MessageId;
import com.github.multiQueue.MultiQueue;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */

public class SendMesToQueueTest {

    @Test
    public void testSend(){
        MultiQueue multiQueue = new MultiQueue();
        multiQueue.initialize();

        for(int i=0;i<100;i++){
            String domain = "domain"+1;
            String ipAddressInHex ="127.0.0."+i;
            int hour=10;
            int index = i+1;
            MessageId messageId = new MessageId(domain,ipAddressInHex,hour,index);
            multiQueue.storeMessage(messageId);
        }

    }

}
