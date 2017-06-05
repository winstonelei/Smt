package com.github.message;

import java.io.Serializable;

/**
 * Created by winstone on 2017/6/5.
 */
public class TestMessageHandler implements  MessageHandler {
    @Override
    public void process(DefaultMessage message) {
        Serializable body = message.getBody();

        System.out.println("TestMessageHandler process message:" + body );
        try {Thread.sleep(500);} catch (Exception e) {}
    }
}
