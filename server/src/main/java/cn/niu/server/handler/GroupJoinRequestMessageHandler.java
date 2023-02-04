package cn.niu.server.handler;


import cn.niu.common.message.GroupCreateResponseMessage;
import cn.niu.common.message.GroupJoinRequestMessage;
import cn.niu.server.common.R;
import cn.niu.server.entity.ChatGroup;
import cn.niu.server.session.factories.ChatGroupSessionServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 用户加入聊天组消息Handler
 *
 * @author Ben
 */

@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String groupName = msg.getGroupName();
        //创建加入群聊回复消息
        GroupCreateResponseMessage groupCreateResponseMessage = null;

        R<Boolean> result = ChatGroupSessionServiceFactory.getChatGroupSessionService().isExist(groupName);
        if (result.getCode() == 1) {
            R<ChatGroup> chatGroupR = ChatGroupSessionServiceFactory.getChatGroupSessionService().joinMember(groupName, username);
            groupCreateResponseMessage = new GroupCreateResponseMessage(chatGroupR.getCode() == 1, "您已进入群聊"+groupName);
        } else {
            groupCreateResponseMessage = new GroupCreateResponseMessage(false, result.getMsg());
        }

        ctx.writeAndFlush(groupCreateResponseMessage);

    }
}
