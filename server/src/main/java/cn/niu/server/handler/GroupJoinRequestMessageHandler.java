package cn.niu.server.handler;

import cn.niu.common.message.GroupCreateRequestMessage;
import cn.niu.common.message.GroupCreateResponseMessage;
import cn.niu.common.message.LoginResponseMessage;
import cn.niu.server.common.R;
import cn.niu.server.entity.ChatGroup;
import cn.niu.server.session.factories.ChatGroupSessionServiceFactory;
import cn.niu.server.session.impl.ChatGroupSessionServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

/**
 * 用户加入聊天组消息Handler
 *
 * @author Ben
 */

@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();

        //创建加入群聊回复消息
        GroupCreateResponseMessage groupCreateResponseMessage = null;

        R<Boolean> result = ChatGroupSessionServiceFactory.getChatGroupSessionService().isExist(groupName);
        if (result.getCode() == 1) {
            R<ChatGroup> chatGroupR = ChatGroupSessionServiceFactory.getChatGroupSessionService().joinMembers(groupName, members);
            groupCreateResponseMessage = new GroupCreateResponseMessage(chatGroupR.getCode() == 1, chatGroupR.getMsg());
        } else {
            groupCreateResponseMessage = new GroupCreateResponseMessage(false, result.getMsg());
        }

        ctx.writeAndFlush(groupCreateResponseMessage);
    }
}
