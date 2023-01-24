package cn.niu.server.session.factories;

import cn.niu.server.session.ChatGroupSessionService;
import cn.niu.server.session.impl.ChatGroupSessionServiceImpl;

/**
 * 聊天组会话管理工厂类
 *
 * @author Ben
 */
public class ChatGroupSessionServiceFactory {
    private static ChatGroupSessionService chatGroupSessionService = new ChatGroupSessionServiceImpl();

    public static ChatGroupSessionService getChatGroupSessionService() {
        return chatGroupSessionService;
    }
}