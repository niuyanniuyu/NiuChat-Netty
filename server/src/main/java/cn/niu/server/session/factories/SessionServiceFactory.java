package cn.niu.server.session.factories;


import cn.niu.server.session.SessionService;
import cn.niu.server.session.impl.SessionServiceImpl;

/**
 * 客户端会话管理工厂类
 *
 * @author Ben
 */
public abstract class SessionServiceFactory {

    private static SessionService sessionService = new SessionServiceImpl();

    public static SessionService getSessionService() {
        return sessionService;
    }
}
