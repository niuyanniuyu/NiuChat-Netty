package cn.niu.common.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
