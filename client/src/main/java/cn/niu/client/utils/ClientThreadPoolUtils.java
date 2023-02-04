package cn.niu.client.utils;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * 客户端输入单一线程池类
 *
 * @author Ben
 */
public class ClientThreadPoolUtils {
    /**
     * 客户端输入线程的线程池
     */
    public static ThreadPoolExecutor threadPoolExecutor;

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 0;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final SynchronousQueue<Runnable> SYNCHRONOUS_QUEUE;
    private static final ThreadFactory THREAD_FACTORY;
    private static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER;


    static {
        SYNCHRONOUS_QUEUE = new SynchronousQueue<>();
        THREAD_FACTORY = new DefaultThreadFactory("system.in");
        REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.AbortPolicy();

        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT,
                SYNCHRONOUS_QUEUE,
                THREAD_FACTORY,
                REJECTED_EXECUTION_HANDLER);
    }
}
