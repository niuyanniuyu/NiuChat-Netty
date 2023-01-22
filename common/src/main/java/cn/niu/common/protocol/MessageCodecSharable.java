package cn.niu.common.protocol;

import cn.niu.common.constant.SerializerTypeConstant;
import cn.niu.common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

/**
 * 统一的消息编解码器
 *
 * @author Ben
 */
@Slf4j
/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    /**
     * 对消息按固定格式(魔数、版本号、序列化方式、消息指令类型、请求序号、长度、padding、内容)编码
     *
     * @param ctx
     * @param msg
     * @param outList
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 4个字节的魔数
        out.writeBytes(new byte[]{'b', 'a', 'b', 'y'});
        // 1个字节的版本号
        out.writeByte(1);
        // 1个字节的序列化方式
        out.writeByte(SerializerTypeConstant.JDK);
        // 1个字节的指令类型，例如登录、注册、私聊、群发等
        out.writeByte(msg.getMessageType());
        // 4个字节的请求序号，预留异步处理消息的能力
        out.writeInt(msg.getSequenceId());

        //获取对象输出字节流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(msg);
            byte[] bytes = bos.toByteArray();

            // 4个字节的长度
            out.writeInt(bytes.length);
            // 由于上述共15字节，添加 padding 填充
            out.writeByte(0xff);

            // 内容
            out.writeBytes(bytes);

            //写入结果
            outList.add(out);

        } catch (Exception e) {
            log.error("编码消息失败", e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("ObjectOutputStream关闭失败", e);
                }
            }
        }
    }

    /**
     * 对消息按固定格式(魔数、版本号、序列化方式、消息指令类型、请求序号、长度、padding、内容)解码
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        int length = in.readInt();
        byte padding = in.readByte();

        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        //反序列化成对象
        Message message = null;
        switch (serializerType) {
            case SerializerTypeConstant.JDK:
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    message = (Message) ois.readObject();
                } catch (ClassNotFoundException e) {
                    log.error("消息反序列化失败", e);
                } catch (IOException e) {
                    log.error("获取ObjectInputStream失败", e);
                } finally {
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e) {
                            log.error("ObjectInputStream关闭失败", e);
                        }
                    }
                }
                break;
            case SerializerTypeConstant.JSON:

                break;
            case SerializerTypeConstant.PROTOBUF:

                break;
            case SerializerTypeConstant.HESSIAN:

                break;
            default:
                log.error("暂无匹配发序列化方法，消息解析失败");
        }

        if (message != null) {
            log.info("消息头信息：{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
            log.info("消息正文：{}", message);

            //存放到Netty的结果集中，给下一个handler使用
            out.add(message);
        }

    }
}
