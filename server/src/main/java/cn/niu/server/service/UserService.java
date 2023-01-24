package cn.niu.server.service;

import cn.niu.server.common.R;
import cn.niu.server.entity.User;

/**
 * 用户管理接口
 *
 * @author Ben
 */
public interface UserService {
    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回 true, 否则返回 false
     */
    R<User> login(String username, String password);

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册成功返回 true, 否则返回 false
     */
    R<User> register(String username, String password);

    /**
     * 判断用户是否存在
     * @param username
     * @return
     */
    boolean isExistUser(String username);

}