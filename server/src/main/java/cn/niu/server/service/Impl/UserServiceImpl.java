package cn.niu.server.service.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.niu.server.common.R;
import cn.niu.server.entity.User;
import cn.niu.server.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理实现类
 *
 * @author Ben
 */
public class UserServiceImpl implements UserService {
    //TODO 暂时使用静态方式实现，后续使用mybatis连接数据库
    /**
     * 静态方式保存用户信息
     */
    Map<String, User> map = new HashMap<>(3);

    {
        map.put("张三", new User("张三", SecureUtil.md5("zhangsan"), "三三"));
        map.put("李四", new User("李四", SecureUtil.md5("lisi"), "四四"));
        map.put("王五", new User("王五", SecureUtil.md5("wangwu"), "五五"));
    }


    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户结果
     */
    @Override
    public R<User> login(String username, String password) {
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            return R.error("账号或密码为空！");
        }

        if (!map.containsKey(username)) {
            return R.error("账号不存在！");
        }

        // 密码md5加密与map存储的对比
        User user = map.get(username);
        if (!SecureUtil.md5(password).equals(user.getPassword())) {
            return R.error("密码错误！");
        }

        return R.success(user,"登陆成功！");
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public R<User> register(String username, String password) {
        return null;
    }
}
