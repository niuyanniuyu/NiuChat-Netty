package cn.niu.server.constants;

/**
 * ServerSocketChannel相关常量
 * @author Ben
 */
public interface ServerSocketChannelConstant {

    /**
     * 全连接队列最大等待Accept数量
     */
    Integer SO_BACKLOG_MAX_VALUE = 1024;

    /**
     * 全连接队列最小等待Accept数量
     */
    Integer SO_BACKLOG_MIN_VALUE = 128;
}

