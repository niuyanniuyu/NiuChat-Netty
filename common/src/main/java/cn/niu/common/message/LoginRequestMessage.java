package cn.niu.common.message;

import lombok.ToString;

/**
 * 登录请求消息
 *
 * @author Ben
 */
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;

    public LoginRequestMessage(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
