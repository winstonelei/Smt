package com.github.multiQueue;

import com.github.messageFileStorage.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author wanglei
 *  初始化多个内存队列，发送消息实现随机队列发送
 * @see [相关类/方法]（可选)
 * @since [产品/模块版本] （可选）
 */
public class MultiQueue {

    private static final Logger logger = LoggerFactory.getLogger(MultiQueue.class);

    private List<BlockingQueue<MessageId>> m_messageQueues = new ArrayList<BlockingQueue<MessageId>>();
    private int m_gzipThreads = 20;
    private int m_gzipMessageSize = 5000;
    private BlockingQueue<MessageId> m_last;

    /**
     * 初始化方法
     */
    public void initialize(){
        for (int i = 0; i < m_gzipThreads; i++) {
            LinkedBlockingQueue<MessageId> messageQueue = new LinkedBlockingQueue<MessageId>(m_gzipMessageSize);
            m_messageQueues.add(messageQueue);
            Threads.forGroup("smt").start(new MessageGzip(messageQueue, i));
        }
        m_last = m_messageQueues.get(m_gzipThreads - 1);
    }

    /**
     * 消息存储
     * @param messageId
     */
    public void storeMessage(MessageId messageId){
        boolean errorFlag = true;
        int hash = Math.abs((messageId.getDomain() + '-' + messageId.getIpAddress()).hashCode());
        int index = (int) (hash % m_gzipThreads);
        int messageQueueIndex = index % (m_gzipThreads - 1);
        logger.info("存储的messageQueue index = "+messageQueueIndex);
        BlockingQueue<MessageId> queue = m_messageQueues.get(messageQueueIndex);
        boolean result = queue.offer(messageId);
        if (result) {
            logger.info("存储消息到messageQueue success ");
            errorFlag = false;
        } else {
            if (m_last.offer(messageId)) {
                errorFlag = false;
            }
        }
    }

    /**
     * 消费消息
     */
    public class MessageGzip implements Threads.Task {

        public BlockingQueue<MessageId> m_messageQueue;

        private int m_index;

        private int m_count = -1;

        public MessageGzip(BlockingQueue<MessageId> messageQueue, int index) {
            m_messageQueue = messageQueue;
            m_index = index;
        }

        @Override
        public String getName() {
            return "Message-Gzip-" + m_index;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    MessageId item = m_messageQueue.poll(5, TimeUnit.MILLISECONDS);

                    if (item != null) {
                        m_count++;
                        if (m_count % (10000) == 0) {
                            System.out.println("10000 条message");
                            //gzipMessageWithMonitor(item);
                        } else {
                    //        gzipMessage(item);
                            logger.info("MessageGzip 消费消息 message="+item.toString());
                        }
                    }
                }
            } catch (InterruptedException e) {
                // ignore it
            }
        }

        @Override
        public void shutdown() {

        }


    }


}
