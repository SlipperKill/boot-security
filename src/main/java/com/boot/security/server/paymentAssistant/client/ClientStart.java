package com.boot.security.server.paymentAssistant.client;

import com.boot.security.server.paymentAssistant.msg.BaseMsg;
import com.boot.security.server.paymentAssistant.msg.LoginData;
import com.boot.security.server.paymentAssistant.msg.MsgType;
import com.boot.security.server.utils.JsonUtil;

import java.util.concurrent.TimeUnit;

public class ClientStart {
    public static void main(String[] args) throws InterruptedException {
        //Scanner input = new Scanner(System.in);
        Client bootstrap = new Client(8084, "175.6.101.244");

        while (true) {
            System.out.println("向服务端发起PING");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BaseMsg pingMsg = new BaseMsg();
            pingMsg.setMsgType(MsgType.PING);
            pingMsg.setClientId("test");
            bootstrap.sendMessage(pingMsg);
        }

//        BaseMsg loginMsg = new BaseMsg();
//        loginMsg.setMsgType(MsgType.LOGIN);
//        LoginData loginData = new LoginData();
//        loginData.setAccount("test");
//        loginData.setPassword("123456");
//        loginMsg.setClientId(loginData.getAccount());
//        loginMsg.setBody(JsonUtil.bean2Json(loginData));
//        bootstrap.sendMessage(loginMsg);
//        TimeUnit.SECONDS.sleep(3);
//
//        bootstrap.sendMessage(loginMsg);
//        TimeUnit.SECONDS.sleep(3);

//        new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    System.out.println("向服务端发起PING");
//                    try {
//                        TimeUnit.SECONDS.sleep(3);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    BaseMsg pingMsg = new BaseMsg();
//                    pingMsg.setMsgType(MsgType.PING);
//                    pingMsg.setClientId("test");
//                    bootstrap.sendMessage(pingMsg);
//                }
//            }
//        }.start();

    }
}
