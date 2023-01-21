package cn.niu.common.message;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有消息类型的公共父类
 * @author Ben
 */
@Data
public abstract class Message implements Serializable {
    /**
     * 根据消息类型获得对应消息类
     * @param messageType
     * @return
     */
    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    /**
     * 顺序Id
     */
    private int sequenceId;

    /**
     * 消息类型
     */
    private int messageType;

    /**
     * 抽象方法，由真正的子类实现，返回具体的消息类型
     * @return
     */
    public abstract int getMessageType();

    /**
     * 注册所有消息类型的版本号
     */
    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();
}
