package com.boot.security.server.paymentAssistant.server;

import com.boot.security.server.paymentAssistant.codec.MsgPckDecode;
import com.boot.security.server.paymentAssistant.codec.MsgPckEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class NettyServer {

    private Logger logger = LoggerFactory.getLogger("adminLogger");

    @Resource
    private ServerHandler serverHandler;

    @Resource
    private NettyConfig nettyConfig;

    private ServerSocketChannel serverSocketChannel;
    private ServerBootstrap bootstrap = new ServerBootstrap();
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();
    private ChannelFuture future = null;

    @PreDestroy
    public void stop() {
        if (future != null) {
            future.channel().close().addListener(ChannelFutureListener.CLOSE);
            future.awaitUninterruptibly();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            future = null;
            logger.debug(" netty服务关闭 ");
        }
    }


    public void start() {
        //服务端要建立两个group，一个负责接收客户端的连接，一个负责处理数据传输
        // 绑定处理group
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                //保持连接数
                .option(ChannelOption.SO_BACKLOG, 128)
                //有数据立即发送
                .option(ChannelOption.TCP_NODELAY, true)
                //保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //处理新连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        // 增加任务处理
                        ChannelPipeline p = sc.pipeline();
                        p.addLast(
                                new IdleStateHandler(NettyConstants.SERVER_READ_IDEL_TIME_OUT, NettyConstants.SERVER_WRITE_IDEL_TIME_OUT, NettyConstants.SERVER_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS),
                                new MsgPckDecode(),
                                new MsgPckEncode(),
                                //自定义的处理器
                                serverHandler);
                    }
                });

        //绑定端口，同步等待成功
        try {
            future = bootstrap.bind(nettyConfig.getPort()).sync();
            if (future.isSuccess()) {
                serverSocketChannel = (ServerSocketChannel) future.channel();
                logger.debug("netty服务在[{}]端口开启成功", nettyConfig.getPort());
            } else {
                logger.debug("netty服务在[{}]端口开启成功", nettyConfig.getPort());
            }

            //等待服务监听端口关闭,就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅地退出，释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    public void sendMessage(Object msg) {
        if (serverSocketChannel != null) {
            serverSocketChannel.writeAndFlush(msg);
        }

    }
}
