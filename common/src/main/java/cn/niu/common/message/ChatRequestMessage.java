package cn.niu.common.message;

import lombok.Data;
import lombok.ToString;

/**
 * 私信请求消息
 *
 * @author Ben
 */
@Data
@ToString(callSuper = true)
public class ChatRequestMessage extends Message {
    /**
     * 内容
     */
    private String content;

    /**
     * 接收方username
     */
    private String to;

    /**
     * 发送方username
     */
    private String from;

    public ChatRequestMessage() {
    }

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}
