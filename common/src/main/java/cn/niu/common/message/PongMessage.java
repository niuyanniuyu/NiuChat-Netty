package cn.niu.common.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class PongMessage extends Message {
    private String message;

    @Override
    public int getMessageType() {
        return PongMessage;
    }

    public PongMessage(String message) {
        this.message = message;
    }
}
