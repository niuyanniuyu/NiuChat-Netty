package cn.niu.common.message;

import lombok.Data;
import lombok.ToString;

/**
 * 私信回复消息
 *
 * @author Ben
 */
@Data
@ToString(callSuper = true)
public class ChatResponseMessage extends AbstractResponseMessage {

    /**
     * 发送方username
     */
    private String from;

    /**
     * 内容
     */
    private String content;

    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
        setSuccess(true);
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
