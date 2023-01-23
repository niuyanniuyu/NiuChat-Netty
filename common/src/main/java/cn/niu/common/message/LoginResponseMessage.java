package cn.niu.common.message;

import lombok.Data;
import lombok.ToString;

/**
 * 登录回复消息
 * @author Ben
 */
@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {
    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
}
