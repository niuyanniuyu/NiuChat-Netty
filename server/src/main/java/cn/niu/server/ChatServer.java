package cn.niu.server;

import cn.niu.common.protocol.MessageCodecSharable;
import cn.niu.common.protocol.ProtocolFrameDecoder;
import cn.niu.server.constants.HeartBeatConstant;
import cn.niu.server.handler.ChatRequestMessageHandler;
import cn.niu.server.handler.IdleHandler;
import cn.niu.server.handler.LoginRequestMessageHandler;
import cn.niu.server.session.impl.SessionServiceFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 服务端主程序
 *
 * @author Ben
 */
@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        log.info("server start..");
        // 定义boss组合worker组
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(6);

        //定义可共享的Handler
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        IdleHandler IDLE_HANDLER = new IdleHandler();
        LoginRequestMessageHandler LOGIN_REQUEST_MESSAGE_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_REQUEST_MESSAGE_HANDLER = new ChatRequestMessageHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //ProtocolFrameDecoder为每个channel独享，不能设置为共有对象
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    //ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    //心跳检查handler，规定时间内没有收到消息就会触发IdleState#READER_IDLE读空闲事件，对写事件服务端暂不检查
                    ch.pipeline().addLast(new IdleStateHandler(HeartBeatConstant.READER_IDLE_TIME_SECONDS, HeartBeatConstant.WRITER_IDLE_TIME_SECONDS, HeartBeatConstant.ALL_IDLE_TIME_SECONDS, TimeUnit.SECONDS));
                    ch.pipeline().addLast(IDLE_HANDLER);

                    ch.pipeline().addLast(LOGIN_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(CHAT_REQUEST_MESSAGE_HANDLER);
                }
            });

            Channel channel = serverBootstrap.bind(8080).sync().channel();
            log.info("server started");
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("服务器异常，{}", e.toString());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
