package com.github.message;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by winstone on 2017/6/5.
 */
public class DefaultMessage  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msgId = UUID.randomUUID().toString();

    private Serializable body;

    public DefaultMessage(){}

    public  DefaultMessage(Serializable body){
        super();
        this.body = body;

    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Serializable getBody() {
        return body;
    }

    public void setBody(Serializable body) {
        this.body = body;
    }



}
