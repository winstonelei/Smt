package com.github.message;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * Created by winstone on 2017/6/5.
 */
public class TestMessage {

    public  static void main(String[] args)throws InterruptedException {

          MessageProcessor processor = new MessageProcessor();

          MessageHandler handler = new TestMessageHandler();

          while(true){
              processor.submit(new DefaultMessage(RandomStringUtils.random(5, true, true)), handler);
              Thread.sleep(RandomUtils.nextLong(100, 500));
          }


    }
}
