package cn.niu.client.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 登陆业务相关工具类
 *
 * @author Ben
 */
public class LoginUtils {
    /**
     * 等待用户登陆的回复消息时阻塞住输入用户名密码的System.in线程继续执行
     */
    public static CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);

    /**
     * 登陆回复消息是否通过验证
     */
    public static AtomicBoolean LOGIN_STATE = new AtomicBoolean(false);

}
