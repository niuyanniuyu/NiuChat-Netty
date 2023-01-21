package cn.niu.constant;

/**
 * 存放序列化方式的常量
 *
 * @author Ben
 */
public interface SerializerTypeConstant {
    /**
     * 使用JDK序列化方式 (暂时使用JDK序列化方式)
     */
    byte JDK = 0;

    /**
     * 使用JSON序列化方式
     */
    byte JSON = 1;

    /**
     * 使用protobuf序列化方式
     */
    byte PROTOBUF = 2;

    /**
     * 使用hessian序列化方式
     */
    byte HESSIAN = 3;
}
