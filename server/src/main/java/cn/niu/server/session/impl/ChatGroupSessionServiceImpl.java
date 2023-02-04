package cn.niu.server.session.impl;

import cn.hutool.core.util.StrUtil;
import cn.niu.server.common.R;
import cn.niu.server.entity.ChatGroup;
import cn.niu.server.service.factories.UserServiceFactory;
import cn.niu.server.session.ChatGroupSessionService;
import cn.niu.server.session.factories.SessionServiceFactory;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 聊天组会话管理实现类
 *
 * @author Ben
 */
@Slf4j
public class ChatGroupSessionServiceImpl implements ChatGroupSessionService {
    /**
     * 基于内存方式保存群聊信息
     */
    private final Map<String, ChatGroup> groupMap = new ConcurrentHashMap<>();

    /**
     * 创建群聊
     *
     * @param groupName 组名
     * @param members   成员
     * @return
     */
    @Override
    public R<ChatGroup> createGroup(String groupName, Set<String> members) {
        //return groupMap.putIfAbsent(groupName, new ChatGroup(groupName, members));

        //判断群名称是否合法
        if (StrUtil.isEmpty(groupName)) {
            log.info("群聊创建失败，群名称为空");
            return R.error("组名不可为空！");
        }

        //判断是否已存在该名称的群聊
        if (groupMap.containsKey(groupName)) {
            log.info("重复创建群聊，群名称:{}", groupName);
            return R.error("该群聊名称已存在");
        }

        //判断member中是否有未存在的用户
        if (members != null) {
            Iterator<String> iterator = members.iterator();
            while (iterator.hasNext()) {
                if (!UserServiceFactory.getUserService().isExistUser(iterator.next())) {
                    iterator.remove();
                }
            }
        }

        //判断成员列表是否为空
        if (members == null || members.size() == 0) {
            log.info("群聊创建失败，成员为空");
            return R.error("群聊成员为空");
        }

        ChatGroup chatGroup = new ChatGroup(groupName, members);
        groupMap.put(groupName, chatGroup);
        log.info("创建群聊成功，群名称:{}，成员:{}", groupName, members);
        return R.success(chatGroup, "创建群聊成功");
    }

    /**
     * 加入群聊
     *
     * @param groupName 组名
     * @param member    成员名
     * @return
     */
    @Override
    public R<ChatGroup> joinMember(String groupName, String member) {
        //判断组名和成员是否为空
        if (!StrUtil.isAllNotEmpty(groupName, member)) {
            log.info("加入群聊时群名称或成员为空，群名称{}，成员{}", groupName, member);
            return R.error("群名称和成员不允许为空");
        }

        //判断当前组是否存在，存在就向ChatGroup的member中添加新成员，不存在返回null
        ChatGroup chatGroup = groupMap.computeIfPresent(groupName, (key, value) -> {
            value.getMembers().add(member);
            return value;
        });

        log.info(chatGroup == null ? "添加失败，群聊不存在" : "添加成功");
        return chatGroup == null ? R.error("添加失败，群聊不存在") : R.success(chatGroup, "添加成功");
    }

    /**
     * 将成员集合加入群聊
     *
     * @param groupName
     * @param memberSet
     * @return
     */
    @Override
    public R<ChatGroup> joinMembers(String groupName, Set<String> memberSet) {
        R<ChatGroup> result = null;
        if (memberSet != null) {
            //遍历用户集合加入群聊
            for (String memberName : memberSet) {
                result = joinMember(groupName, memberName);
                if (result.getCode() != 1) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 移除组成员
     *
     * @param groupName 组名
     * @param member    成员名
     * @return
     */
    @Override
    public R<ChatGroup> removeMember(String groupName, String member) {
        //判断组名和成员是否为空
        if (StrUtil.isAllNotEmpty(groupName, member)) {
            log.info("移除群聊时群名称或成员为空，群名称{}，成员{}", groupName, member);
            return R.error("群名称和成员不允许为空");
        }

        //从群聊中删除用户，若不存在该群聊则返回null
        ChatGroup chatGroup = groupMap.computeIfPresent(groupName, (key, value) -> {
            value.getMembers().remove(member);
            return value;
        });

        log.info(chatGroup == null ? "删除失败，群聊不存在" : "删除成功");
        return chatGroup == null ? R.error("删除失败，群聊不存在") : R.success(chatGroup, "删除成功");
    }

    /**
     * 移除聊天组
     *
     * @param groupName 组名
     * @return
     */
    @Override
    public R<ChatGroup> removeGroup(String groupName) {
        if (StrUtil.isEmpty(groupName)) {
            log.info("群名称为空");
            return R.error("群名称为空");
        }
        ChatGroup chatGroup = groupMap.remove(groupName);

        //判断是否删除成功
        if (chatGroup == null) {
            log.info("删除群聊失败，群名称{}", groupName);
            return R.error("删除失败");
        }

        log.info("删除群聊成功，群名称{}", groupName);
        return R.success(chatGroup, "删除成功");
    }

    /**
     * 获取组成员
     *
     * @param groupName 组名
     * @return
     */
    @Override
    public R<Set<String>> getMembers(String groupName) {
        if (StrUtil.isEmpty(groupName)) {
            log.info("群名称为空");
            R.error("群名称为空");
        }
        return R.success(groupMap.getOrDefault(groupName, ChatGroup.EMPTY_GROUP).getMembers());
    }

    /**
     * 获取组成员的 channel 集合, 只有在线的 channel 才会返回
     *
     * @param groupName 组名
     * @return
     */
    @Override
    public R<List<Channel>> getMembersChannel(String groupName) {
        if (StrUtil.isEmpty(groupName)) {
            log.info("群名称为空");
            return R.error("群名称为空");
        }

        R<Set<String>> members = getMembers(groupName);
        if (members.getCode() == 1 && members.getData().size() != 0) {
            return R.success(members.getData().stream()
                    .map(member -> SessionServiceFactory.getSessionService().getChannel(member))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()), "查找成功");
        }
        return R.error("获取channel失败");
    }

    /**
     * 根据组名称获取聊天组
     *
     * @param groupName
     * @return
     */
    @Override
    public R<ChatGroup> getChatGroup(String groupName) {
        if (!StrUtil.isEmpty(groupName) && isExist(groupName).getCode() == 1) {
            return R.success(groupMap.get(groupName), "查找成功");
        }
        return R.error("用户组不存在");
    }

    /**
     * 判断群聊是否存在
     *
     * @param groupName
     * @return
     */
    @Override
    public R<Boolean> isExist(String groupName) {
        if (!StrUtil.isEmpty(groupName) && groupMap.containsKey(groupName)) {
            return R.success(true, "用户组存在");
        }
        return R.error("用户组不存在");
    }
}