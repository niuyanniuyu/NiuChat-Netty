package cn.niu.server.handler;


import cn.niu.server.session.SessionService;
import cn.niu.server.session.factories.SessionServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理客户端断开连接handler
 *
 * @author Ben
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当连接断开时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionService sessionService = SessionServiceFactory.getSessionService();
        Channel channel = ctx.channel();
        String username = sessionService.getUsername(channel);
        //要写在后面，不然获取不了用户名
        sessionService.unbind(channel);
        log.debug("{}已经断开", username);
    }

    /**
     * 当发生异常时触发
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SessionService sessionService = SessionServiceFactory.getSessionService();
        Channel channel = ctx.channel();
        String username = sessionService.getUsername(channel);
        sessionService.unbind(channel);
        log.debug("{}异常断开，异常是:{}", username, cause.getMessage());
    }
}
