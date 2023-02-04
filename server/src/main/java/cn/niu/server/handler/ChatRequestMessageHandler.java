package cn.niu.server.handler;

import cn.niu.common.message.ChatRequestMessage;
import cn.niu.common.message.ChatResponseMessage;
import cn.niu.server.constants.ChatConstant;
import cn.niu.server.session.factories.SessionServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理聊天信息的Handler
 *
 * @author Ben
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) {
        String to = msg.getTo();
        Channel toChannel = SessionServiceFactory.getSessionService().getChannel(to);
        if (toChannel != null) {
            //对方在线
            toChannel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else {
            //对方不在线，返回发送失败消息
            //TODO 离线消息，用数据保存
            ctx.channel().writeAndFlush(new ChatResponseMessage(false, ChatConstant.USER_IS_NOT_ONLINE_OR_EXIST));
        }
    }
}
