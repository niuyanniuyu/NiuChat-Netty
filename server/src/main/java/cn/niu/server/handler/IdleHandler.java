package cn.niu.server.handler;

import cn.niu.server.constants.HeartBeatConstant;
import cn.niu.server.session.impl.SessionServiceFactory;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于Netty提供的心跳检测Idle读、写双向的handler
 *
 * @author Ben
 */

@Slf4j
@ChannelHandler.Sharable
public class IdleHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断事件是否属于IdleStateEvent心跳检测类型的
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            String username = SessionServiceFactory.getSessionService().getUsername(ctx.channel());

            //对不同类型进行处理
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    log.info("用户 {} 已经 {}s 没有发送数据", username, HeartBeatConstant.READER_IDLE_TIME_SECONDS);
                    break;
                case WRITER_IDLE:
                    log.info("已经 {}s 没有向用户 {} 发送数据", HeartBeatConstant.WRITER_IDLE_TIME_SECONDS, username);
                    break;
                case ALL_IDLE:
                    log.info("服务端与用户 {} 已经 {}s 没有通信过", username, HeartBeatConstant.ALL_IDLE_TIME_SECONDS);
                    break;
                default:
                    log.info("类型 {} 错误，无法处理", idleStateEvent.state());
                    break;
            }
        }
    }
}