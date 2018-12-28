package com.boot.security.server.paymentAssistant.msg;

import org.msgpack.annotation.Message;

import java.io.Serializable;

@Message
public class BaseMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    private int msgType;

    private String clientId;

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
