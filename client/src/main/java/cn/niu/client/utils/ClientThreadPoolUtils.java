package cn.niu.client.utils;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * 客户端输入单一线程池类
 *
 * @author Ben
 */
public class ClientThreadPoolUtils {
    public static ThreadPoolExecutor threadPoolExecutor;

    private static final int corePoolSize = 1;
    private static final int maximumPoolSize = 1;
    private static final long keepAliveTime = 0;
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;
    private static final SynchronousQueue synchronousQueue;
    private static final ThreadFactory threadFactory;
    private static final RejectedExecutionHandler rejectedExecutionHandler;


    static {
        synchronousQueue = new SynchronousQueue();
        threadFactory = new DefaultThreadFactory("system.in");
        rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
                synchronousQueue,
                threadFactory,
                rejectedExecutionHandler);
    }
}
