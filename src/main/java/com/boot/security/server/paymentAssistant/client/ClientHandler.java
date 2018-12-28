package com.boot.security.server.paymentAssistant.client;

import com.boot.security.server.paymentAssistant.msg.AccountData;
import com.boot.security.server.paymentAssistant.msg.LoginData;
import com.boot.security.server.paymentAssistant.server.NettyChannelMap;
import com.boot.security.server.paymentAssistant.msg.BaseMsg;
import com.boot.security.server.paymentAssistant.msg.MsgType;
import com.boot.security.server.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.TimeUnit;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端连接关闭...");
        //channel失效，从Map中移除
        NettyChannelMap.remove((SocketChannel) ctx.channel());
        //NettyConfig.group.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BaseMsg baseMsg = (BaseMsg) msg;
        switch (baseMsg.getMsgType()) {
            case MsgType.LOGIN: {
                System.out.println("客户端 login.......");
                BaseMsg loginMsg = new BaseMsg();
                loginMsg.setMsgType(MsgType.LOGIN);
                LoginData loginData = new LoginData();
                loginData.setAccount("test");
                loginData.setPassword("123456");
                loginMsg.setClientId(loginData.getAccount());
                loginMsg.setBody(JsonUtil.bean2Json(loginData));
                ctx.channel().writeAndFlush(loginMsg);
            }
            break;
            case MsgType.LOGIN_SUCCESS: {
                System.out.println(baseMsg.getBody());
                BaseMsg loginMsg = new BaseMsg();
                loginMsg.setMsgType(MsgType.ACCOUNT);
                AccountData accountData = new AccountData();
                accountData.setAccount("287091247@qq.com");
                accountData.setAccuntType("alipay");
                accountData.setUserName("拖鞋斩");
                loginMsg.setClientId("test");
                loginMsg.setBody(JsonUtil.bean2Json(accountData));
                ctx.channel().writeAndFlush(loginMsg);

            }break;
            case MsgType.FAILD: {
                System.out.println(baseMsg.getBody());
            }
            break;
            default: {
            }
            break;
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道读取完毕！");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (null != cause) {
            cause.printStackTrace();
        }
        if (null != ctx) {
            ctx.close();
        }
    }

}
