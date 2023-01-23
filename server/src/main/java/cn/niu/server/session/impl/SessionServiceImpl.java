package cn.niu.server.session.impl;

import cn.niu.server.session.SessionService;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端会话管理接口实现类（基于内存实现）
 *
 * @author Ben
 */
public class SessionServiceImpl implements SessionService {
    /**
     * 根据用户名查询channel
     */
    private final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();
    /**
     * 根据channel查询用户名
     */
    private final Map<Channel, String> channelUsernameMap = new ConcurrentHashMap<>();
    /**
     * channel和自己的属性绑定
     */
    private final Map<Channel, Map<String, Object>> channelAttributesMap = new ConcurrentHashMap<>();


    /**
     * 绑定用户和channel
     *
     * @param channel  哪个 channel 要绑定会话
     * @param username 会话绑定用户
     */
    @Override
    public void bind(Channel channel, String username) {
        usernameChannelMap.put(username, channel);
        channelUsernameMap.put(channel, username);
    }

    /**
     * 解绑channel和会话
     *
     * @param channel 哪个 channel 要解绑会话
     */
    @Override
    public void unbind(Channel channel) {

    }

    /**
     * 获取channel某个属性(扩展用)
     *
     * @param channel 哪个 channel
     * @param name    属性名
     * @return
     */
    @Override
    public Object getAttribute(Channel channel, String name) {
        return null;
    }

    /**
     * 根据名称获取channel的属性(扩展用)
     *
     * @param channel 哪个 channel
     * @param name    属性名
     * @param value   属性值
     */
    @Override
    public void setAttribute(Channel channel, String name, Object value) {

    }

    /**
     * 根据用户名获取channel
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Channel getChannel(String username) {
        return null;
    }

    /**
     * 根据channel获取用户名
     *
     * @param channel
     * @return
     */
    @Override
    public String getUsername(Channel channel) {
        return null;
    }
}
