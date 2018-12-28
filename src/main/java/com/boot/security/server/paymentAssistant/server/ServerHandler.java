package com.boot.security.server.paymentAssistant.server;

import com.boot.security.server.dao.CodeManDao;
import com.boot.security.server.dao.PayAccountDao;
import com.boot.security.server.model.CodeMan;
import com.boot.security.server.model.PayAccount;
import com.boot.security.server.paymentAssistant.msg.AccountData;
import com.boot.security.server.paymentAssistant.msg.BaseMsg;
import com.boot.security.server.paymentAssistant.msg.LoginData;
import com.boot.security.server.paymentAssistant.msg.MsgType;
import com.boot.security.server.utils.JsonUtil;
import com.boot.security.server.utils.UUIDUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Auth
 */
@ChannelHandler.Sharable
@Component
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Resource
    private CodeManDao codeManDao;

    @Resource
    private PayAccountDao payAccountDao;

    private Logger logger = LoggerFactory.getLogger("adminLogger");

    /**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("客户端与服务端连接开始...");
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        //channel失效，从Map中移除
        String clientId = NettyChannelMap.remove((SocketChannel) ctx.channel());
        logger.debug("客户端: {} 与服务端连接关闭...", clientId);
        payAccountOffLine(clientId);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            if (state == IdleState.READER_IDLE) {
                System.out.println(String.valueOf(NettyConstants.SERVER_READ_IDEL_TIME_OUT) + "秒没有接收到客户端的信息了");
                System.out.println("------------服务器主动关闭客户端链路-----");
                String clientId = NettyChannelMap.remove((SocketChannel) ctx.channel());
                ctx.channel().close();
                payAccountOffLine(clientId);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        logger.debug("信息接收完毕...");
    }

    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 服务端处理客户端websocket请求的核心方法，这里接收了客户端发来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object info) throws Exception {

        BaseMsg msg = (BaseMsg) info;
        int type = msg.getMsgType();
        if (MsgType.LOGIN == type) {
            logger.debug("接收到clientId:{}登录请求", msg.getClientId());
            LoginData loginData = JsonUtil.json2Bean(msg.getBody(), LoginData.class);
            CodeMan codeMan = codeManDao.login(loginData.getAccount(), loginData.getPassword());
            if (codeMan == null) {
                logger.debug("client:{} 登录失败", msg.getClientId());
                BaseMsg faildMsg = new BaseMsg();
                faildMsg.setMsgType(MsgType.FAILD);
                faildMsg.setBody("用户名或密码错误");
                channelHandlerContext.channel().writeAndFlush(faildMsg);
            } else {
                //登录成功,把channel存到服务端的map中
                NettyChannelMap.add(msg.getClientId(), (SocketChannel) channelHandlerContext.channel());
                logger.debug("client:{} 登录成功", msg.getClientId());
                BaseMsg successMsg = new BaseMsg();
                successMsg.setMsgType(MsgType.LOGIN_SUCCESS);
                successMsg.setBody("登录成功");
                channelHandlerContext.channel().writeAndFlush(successMsg);
            }
        } else {
            if (NettyChannelMap.get(msg.getClientId()) == null) {
                logger.debug("clientId 为空,需要登录");
                //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
                BaseMsg loginMsg = new BaseMsg();
                loginMsg.setMsgType(MsgType.LOGIN);
                channelHandlerContext.channel().writeAndFlush(loginMsg);
            } else {
                switch (type) {
                    case MsgType.ACCOUNT: {
                        logger.debug("接受到clientId:{} 账号上线请求", msg.getClientId());
                        AccountData accountMsg = JsonUtil.json2Bean(msg.getBody(), AccountData.class);
                        logger.debug("client id:" + msg.getClientId() + ",body:" + msg.getBody());
                        PayAccount payAccount = payAccountDao.getByAccount(accountMsg.getAccount(), accountMsg.getAccuntType());
                        if (payAccount == null) {
                            payAccount = new PayAccount();
                            payAccount.setId(UUIDUtil.getUUID());
                            payAccount.setStatus(0);
                            payAccount.setAccountType(accountMsg.getAccuntType());
                            payAccount.setPayAccount(accountMsg.getAccount());
                            payAccount.setLevel(1);
                            payAccount.setCodeMan(msg.getClientId());
                            payAccount.setOnlineTime(new Date());
                            payAccount.setUserName(accountMsg.getUserName());
                            payAccount.setCreateTime(new Date());
                            payAccountDao.save(payAccount);
                        } else {
                            payAccount.setUserName(accountMsg.getUserName());
                            payAccount.setStatus(0);
                            payAccount.setOnlineTime(new Date());
                            payAccount.setCodeMan(msg.getClientId());
                            payAccountDao.update(payAccount);
                        }

                        BaseMsg successMsg = new BaseMsg();
                        successMsg.setMsgType(MsgType.ONLINE_SUCCESS);
                        successMsg.setBody("account:" + accountMsg.getAccount() + " is online");
                        NettyChannelMap.get(msg.getClientId()).writeAndFlush(successMsg);
                    }
                    break;
                    case MsgType.PING: {
                        logger.debug("client id:" + msg.getClientId() + " is online!");
                    }
                    break;
                    default:
                        break;
                }
            }

        }

        ReferenceCountUtil.release(info);
    }

    private void payAccountOffLine(String clientId) {
        PayAccount payAccount = payAccountDao.getByCodeMan(clientId);
        if (payAccount != null) {
            payAccount.setCodeMan(clientId);
            payAccount.setStatus(1);
            payAccount.setOfflineTime(new Date());
            payAccountDao.update(payAccount);
        }
    }
}
