package cn.niu.client.constants;

/**
 * 客户端心跳检测相关常量
 *
 * @author Ben
 */
public interface HeartBeatConstant {
    /**
     * 为读超时时间（即测试端一定时间内未接受到被测试端消息)
     */
    Integer READER_IDLE_TIME_SECONDS = 0;

    /**
     * 为写超时时间（即测试端一定时间内未向被测试端发送消息）
     * 保活消息一般等于服务器读超时时间的一半
     */
    Integer WRITER_IDLE_TIME_SECONDS = 60;

    /**
     * 所有类型的超时时间
     * 0为暂不做处理
     */
    Integer ALL_IDLE_TIME_SECONDS = 0;
}
