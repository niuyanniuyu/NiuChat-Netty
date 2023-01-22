package cn.niu.server.session;

import cn.niu.server.common.R;
import cn.niu.server.entity.ChatGroup;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * 聊天组会话管理接口
 *
 * @author Ben
 */
public interface ChatGroupSessionService {
    /**
     * 创建一个聊天组, 如果不存在才能创建成功, 否则返回一个实际的值
     *
     * @param groupName   组名
     * @param membersName 成员
     * @return 成功时返回null, 失败返回实际的值
     */
    R<ChatGroup> createGroup(String groupName, Set<String> membersName);

    /**
     * 加入聊天组
     *
     * @param groupName  组名
     * @param memberName 成员名
     * @return 如果组不存在返回 null, 否则返回组对象
     */
    R<ChatGroup> joinMember(String groupName, String memberName);

    /**
     * 移除组成员
     *
     * @param groupName  组名
     * @param memberName 成员名
     * @return 如果组不存在返回 null, 否则返回组对象
     */
    R<ChatGroup> removeMember(String groupName, String memberName);

    /**
     * 移除聊天组
     *
     * @param groupName 组名
     * @return 如果组不存在返回 null, 否则返回组对象
     */
    R<ChatGroup> removeGroup(String groupName);

    /**
     * 获取组成员
     *
     * @param groupName 组名
     * @return 成员集合, 没有成员会返回 empty set
     */
    R<Set<String>> getMembers(String groupName);

    /**
     * 获取组成员的 channel 集合, 只有在线的 channel 才会返回
     *
     * @param groupName 组名
     * @return 成员 channel 集合
     */
    R<List<Channel>> getMembersChannel(String groupName);
}
