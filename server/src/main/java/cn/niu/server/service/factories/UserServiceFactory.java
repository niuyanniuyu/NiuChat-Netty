package cn.niu.server.service.factories;


import cn.niu.server.service.Impl.UserServiceImpl;
import cn.niu.server.service.UserService;

/**
 * UserService工厂类
 * @author Ben
 */
public class UserServiceFactory {
    
    private static UserService userService = new UserServiceImpl();

    public static UserService getUserService() {
        return userService;
    }
}
