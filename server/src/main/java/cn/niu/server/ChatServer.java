package cn.niu.server;

import cn.niu.common.protocol.MessageCodecSharable;
import cn.niu.common.protocol.ProtocolFrameDecoder;
import cn.niu.server.handler.ChatRequestMessageHandler;
import cn.niu.server.handler.GroupChatRequestMessageHandler;
import cn.niu.server.handler.GroupCreatRequestMessageHandler;
import cn.niu.server.handler.LoginRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

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
        LoginRequestMessageHandler LOGIN_REQUEST_MESSAGE_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_REQUEST_MESSAGE_HANDLER = new ChatRequestMessageHandler();
        GroupCreatRequestMessageHandler GROUP_CREATE_REQUEST_MESSAGE_HANDLER = new GroupCreatRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_REQUEST_MESSAGE_HANDLER = new GroupChatRequestMessageHandler();
        //TODO 其他类型Handler


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
                    ch.pipeline().addLast(LOGIN_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(CHAT_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_CREATE_REQUEST_MESSAGE_HANDLER);
                    ch.pipeline().addLast(GROUP_CHAT_REQUEST_MESSAGE_HANDLER);
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
