package cn.niu.client;

import cn.hutool.core.util.StrUtil;
import cn.niu.client.utils.LoginUtils;
import cn.niu.common.message.*;
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

import java.util.*;

/**
 * 客户端主函数
 *
 * @author Ben
 */
@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        log.info("client start..");
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
                    // ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            //在连接建立后触发Active事件，发送登录消息
                            new Thread(() -> {
                                //负责接收用户在控制台的输入并发送各种消息
                                Scanner scanner = new Scanner(System.in);
                                System.out.print("请输入用户名：");
                                String username = scanner.nextLine();
                                System.out.print("请输入密码：");
                                String password = scanner.nextLine();
                                if (StrUtil.isAllNotEmpty(username, password)) {
                                    //构造LoginMessage消息对象
                                    LoginRequestMessage message = new LoginRequestMessage(username, password);
                                    ctx.writeAndFlush(message);
                                    //等待回复消息再向下执行
                                    try {
                                        LoginUtils.WAIT_FOR_LOGIN.await();
                                    } catch (InterruptedException e) {
                                        log.error("登陆线程中断,{}", e.toString());
                                    }
                                    //判断登录状态，失败直接退出客户端
                                    if (!LoginUtils.LOGIN_STATE.get()) {
                                        ctx.channel().close();
                                        return;
                                    }

                                    while (true) {
                                        System.out.println("=================");
                                        System.out.println("send [username][content]");
                                        System.out.println("gsend [group name][content]");
                                        System.out.println("gcreate [group name][m1,m2,m3...]");
                                        System.out.println("gmembers [group name]");
                                        System.out.println("gjoin [group name]");
                                        System.out.println("gquit [group name]");
                                        System.out.println("quit");
                                        System.out.println("=================");
                                        String in = scanner.nextLine();
                                    }
                                }
                            }, "system.in").start();
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            //当回复消息类型时登陆时，判断是否通过用户验证
                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage responseMessage = (LoginResponseMessage) msg;
                                log.info(responseMessage.getReason());
                                if (responseMessage.isSuccess()) {
                                    LoginUtils.LOGIN_STATE.set(true);
                                }
                                // 使System.in线程继续执行下一次输入
                                LoginUtils.WAIT_FOR_LOGIN.countDown();
                            }
                            super.channelRead(ctx, msg);
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            log.info("client started");
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("客户端异常，{}", e.toString());
        } finally {
            group.shutdownGracefully();
            log.info("client closed");
        }
    }
}
