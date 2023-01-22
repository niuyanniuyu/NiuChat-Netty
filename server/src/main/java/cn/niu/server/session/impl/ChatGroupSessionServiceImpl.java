package cn.niu.server.session.impl;

import cn.niu.server.common.R;
import cn.niu.server.entity.ChatGroup;
import cn.niu.server.session.ChatGroupSessionService;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * 聊天组会话管理接口实现类
 * @author Ben
 */
public class ChatGroupSessionServiceImpl implements ChatGroupSessionService {
    /**
     * 创建聊天组
     * @param groupName   组名
     * @param membersName 成员
     * @return
     */
    @Override
    public R<ChatGroup> createGroup(String groupName, Set<String> membersName) {
        return null;
    }

    /**
     * 成员加入聊天组
     * @param groupName  组名
     * @param memberName 成员名
     * @return
     */
    @Override
    public R<ChatGroup> joinMember(String groupName, String memberName) {
        return null;
    }

    /**
     * 成员退出聊天组
     * @param groupName  组名
     * @param memberName 成员名
     * @return
     */
    @Override
    public R<ChatGroup> removeMember(String groupName, String memberName) {
        return null;
    }

    /**
     * 移除聊天组
     * @param groupName 组名
     * @return
     */
    @Override
    public R<ChatGroup> removeGroup(String groupName) {
        return null;
    }

    /**
     * 获取聊天组所有成员
     * @param groupName 组名
     * @return
     */
    @Override
    public R<Set<String>> getMembers(String groupName) {
        return null;
    }

    /**
     * 获取组成员的channel，用于发送所有人消息
     * @param groupName 组名
     * @return
     */
    @Override
    public R<List<Channel>> getMembersChannel(String groupName) {
        return null;
    }
}
