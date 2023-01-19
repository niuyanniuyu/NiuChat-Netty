package cn.niu.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {
    private String username;//用户名
    private String password;//密码
//    private String nickname;//昵称

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password/*, String nickname*/) {
        this.username = username;
        this.password = password;
//        this.nickname = nickname;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
