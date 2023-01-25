package cn.niu.client.handler;

import cn.niu.client.constants.HeartBeatConstant;
import cn.niu.common.message.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端心跳检测，读写空闲handler
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

            //对不同类型进行处理
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    log.info("已经 {}s 没有接受到服务端数据", HeartBeatConstant.READER_IDLE_TIME_SECONDS);
                    break;
                case WRITER_IDLE:
                    log.info("已经 {}s 没有向服务端发送数据，发送心跳包", HeartBeatConstant.WRITER_IDLE_TIME_SECONDS);
                    ctx.writeAndFlush(new PingMessage());
                    break;
                case ALL_IDLE:
                    log.info("已经 {}s 没有与服务端通信过", HeartBeatConstant.ALL_IDLE_TIME_SECONDS);
                    break;
                default:
                    log.info("类型 {} 错误，无法处理", idleStateEvent.state());
                    break;
            }
        }
    }
}
