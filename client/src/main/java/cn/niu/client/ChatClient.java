package cn.niu.client;

import cn.hutool.core.util.StrUtil;
import cn.niu.common.message.LoginRequestMessage;
import cn.niu.common.protocol.MessageCodecSharable;
import cn.niu.common.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 客户端主函数
 *
 * @author Ben
 */
@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        //定义线程组
        NioEventLoopGroup group = new NioEventLoopGroup();

        //定义handler
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            //在连接建立后触发Active事件，发送登录消息
                            new Thread(() -> {
                                //负责接收用户在控制台的输入并发送各种消息
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名：");
                                String username = scanner.nextLine();
                                System.out.println("请输入密码：");
                                String password = scanner.nextLine();
                                if (StrUtil.isAllNotEmpty(username, password)) {
                                    //构造LoginMessage消息对象
                                    LoginRequestMessage message = new LoginRequestMessage(username, password);
                                    ctx.writeAndFlush(message);

                                }
                            }, "system.in").start();
                            super.channelActive(ctx);
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("客户端异常，{}", e.toString());
        } finally {
            group.shutdownGracefully();
        }
    }
}
