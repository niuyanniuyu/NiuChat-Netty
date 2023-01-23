package cn.niu.server.handler;

import cn.niu.common.message.LoginRequestMessage;
import cn.niu.common.message.LoginResponseMessage;
import cn.niu.server.common.R;
import cn.niu.server.entity.User;
import cn.niu.server.service.factories.UserServiceFactory;
import cn.niu.server.session.impl.SessionServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理登录消息的Handler
 * @author Ben
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) {
        String username = msg.getUsername();
        String password = msg.getPassword();
        R<User> result = UserServiceFactory.getUserService().login(username, password);
        //创建登录返回消息
        LoginResponseMessage loginResponseMessage = new LoginResponseMessage(result.getCode() == 1, result.getMsg());
        //如果用户登录成功则绑定用户名和channel
        if (result.getCode() == 1) {
            SessionServiceFactory.getSessionService().bind(ctx.channel(), username);
        }

        ctx.writeAndFlush(loginResponseMessage);

    }
}
