package cn.niu.test;

import cn.niu.common.message.LoginRequestMessage;
import cn.niu.common.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

/**
 * 测试类：用于common模块的测试
 */
public class TestMain {
    /**
     * 测试编码器
     */
    @Test
    public void testOutBound() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec());
        channel.writeOutbound(new LoginRequestMessage("张三", "123", "sansan"));
    }

    /**
     * 测试解码器
     */
    @Test
    public void testInBound() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec());
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, new LoginRequestMessage("张三", "123", "sansan"), buffer);
        channel.writeInbound(buffer);
    }
}
