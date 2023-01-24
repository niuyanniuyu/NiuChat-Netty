package cn.niu.server.handler;

import cn.niu.common.message.GroupChatRequestMessage;
import cn.niu.common.message.GroupChatResponseMessage;
import cn.niu.common.message.GroupCreateResponseMessage;
import cn.niu.server.common.R;
import cn.niu.server.constants.ChatConstant;
import cn.niu.server.constants.GroupConstant;
import cn.niu.server.session.factories.ChatGroupSessionServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * 处理发送群聊消息handler
 *
 * @author Ben
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        //获取群聊所有用户的Channel
        R<List<Channel>> result = ChatGroupSessionServiceFactory.getChatGroupSessionService().getMembersChannel(msg.getGroupName());
        if (result.getCode() == 1) {
            List<Channel> channelList = result.getData();
            //向每一个成员发送消息
            for (Channel channel : channelList) {
                channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
            }
        } else {
            ctx.writeAndFlush(new GroupChatResponseMessage(false, GroupConstant.FAILED_TO_SEND_GROUP_MESSAGE));
        }


    }
}
