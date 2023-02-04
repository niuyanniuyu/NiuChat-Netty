package cn.niu.server.session.impl;

import cn.niu.server.session.SessionService;
import io.netty.channel.Channel;

import java.util.HashMap;
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
     * 解绑channel和用户会话
     *
     * @param channel 哪个 channel 要解绑会话
     */
    @Override
    public void unbind(Channel channel) {
        channel.close();
        String username = channelUsernameMap.get(channel);
        channelUsernameMap.remove(channel);
        usernameChannelMap.remove(username);
    }

    /**
     * 解绑用户会话和channel
     *
     * @param username 哪个 username 要解绑会话
     */
    @Override
    public void unbind(String username) {
        Channel channel = usernameChannelMap.get(username);
        channel.close();
        channelUsernameMap.remove(channel);
        usernameChannelMap.remove(username);
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
        return channelAttributesMap.get(channel).get(name);
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
        Map<String, Object> attribute = new HashMap<>(2);
        attribute.put(name, value);
        channelAttributesMap.put(channel, attribute);
    }

    /**
     * 根据用户名获取channel
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Channel getChannel(String username) {
        return usernameChannelMap.get(username);
    }

    /**
     * 根据channel获取用户名
     *
     * @param channel
     * @return
     */
    @Override
    public String getUsername(Channel channel) {
        return channelUsernameMap.get(channel);
    }
}
