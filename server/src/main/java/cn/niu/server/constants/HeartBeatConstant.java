package cn.niu.server.constants;

/**
 * 保存心跳检测相关常量
 *
 * @author Ben
 */
public interface HeartBeatConstant {
    /**
     * 为读超时时间（即测试端一定时间内未接受到被测试端消息)
     */
    Integer READER_IDLE_TIME_SECONDS = 5;

    /**
     * 为写超时时间（即测试端一定时间内未向被测试端发送消息）
     */
    Integer WRITER_IDLE_TIME_SECONDS = 60;

    /**
     * 所有类型的超时时间
     * 0为暂不做处理
     */
    Integer ALL_IDLE_TIME_SECONDS = 0;
}
