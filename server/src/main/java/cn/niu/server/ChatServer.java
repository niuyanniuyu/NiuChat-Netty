package cn.niu.server;

import cn.niu.common.config.CommonConfig;
import cn.niu.common.protocol.MessageCodecSharable;
import cn.niu.common.protocol.ProtocolFrameDecoder;
import cn.niu.server.constants.HeartBeatConstant;
import cn.niu.server.constants.ServerSocketChannelConstant;
import cn.niu.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
        GroupCreatRequestMessageHandler GROUP_CREATE_REQUEST_MESSAGE_HANDLER = new GroupCreatRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_REQUEST_MESSAGE_HANDLER = new GroupChatRequestMessageHandler();
        GroupJoinRequestMessageHandler GROUP_JOIN_REQUEST_MESSAGE_HANDLER = new GroupJoinRequestMessageHandler();
        QuitHandler QUIT_HANDLER= new QuitHandler();
        //TODO 其他类型Handler


        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            //设置最大等待连接数量
            serverBootstrap.option(ChannelOption.SO_BACKLOG, ServerSocketChannelConstant.SO_BACKLOG_MAX_VALUE);
            serverBootstrap.group(boss, worker);

            //立即发送消息，不使用nagle算法
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
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
                    ch.pipeline().addLast(GROUP_CREATE_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_CHAT_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_JOIN_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(QUIT_HANDLER);
                }
            });

            //绑定端口号
            Channel channel = serverBootstrap.bind(CommonConfig.getServerPort()).sync().channel();
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
