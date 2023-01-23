package cn.niu.server.session.impl;


import cn.niu.server.session.SessionService;

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
