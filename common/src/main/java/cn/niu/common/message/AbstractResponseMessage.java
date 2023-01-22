package cn.niu.common.message;

import lombok.Data;
import lombok.ToString;

/**
 * 回复消息的抽象父类
 * @author Ben
 */
@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    /**
     * 成功或者失败
     */
    private boolean success;
    /**
     * 成功或错误的提示信息
     */
    private String reason;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
