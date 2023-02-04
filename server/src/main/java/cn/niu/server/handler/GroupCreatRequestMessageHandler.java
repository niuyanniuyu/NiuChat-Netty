package cn.niu.server.handler;

import cn.niu.common.message.GroupChatRequestMessage;
import cn.niu.common.message.GroupCreateRequestMessage;
import cn.niu.common.message.GroupCreateResponseMessage;
import cn.niu.server.common.R;
import cn.niu.server.constants.GroupConstant;
import cn.niu.server.entity.ChatGroup;
import cn.niu.server.session.factories.ChatGroupSessionServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * 处理创建群聊消息handler
 *
 * @author Ben
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupCreatRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        R<ChatGroup> result = ChatGroupSessionServiceFactory.getChatGroupSessionService().createGroup(groupName, members);
        //发送创建群聊回复消息
        ctx.writeAndFlush(new GroupCreateResponseMessage(result.getCode() == 1, result.getMsg()));

        //创建成功需要向群聊成员发送入群消息
        if (result.getCode() == 1) {
            R<List<Channel>> membersChannelResult = ChatGroupSessionServiceFactory.getChatGroupSessionService().getMembersChannel(groupName);
            if (membersChannelResult.getCode() == 1 && membersChannelResult.getData().size() > 0) {
                for (Channel channel : membersChannelResult.getData()) {
                    channel.writeAndFlush(new GroupCreateResponseMessage(true, GroupConstant.HAVE_JOINED_GROUP_CHAT + groupName));
                }
            }

        }
    }
}
