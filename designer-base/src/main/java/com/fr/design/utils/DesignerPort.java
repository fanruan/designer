package com.fr.design.utils;

/**
 * 为的就是能替换 DesignPort.class 实现多开,因此避免编译器常量编译展开优化
 */
public class DesignerPort {
    private DesignerPort() {
    }

    /**
     * 设计器端口，避免编译期常量优化展开
     */
    public static final int MESSAGE_PORT = getMessagePort();
    /**
     * 设计器端口，避免编译期常量优化展开
     */
    public static final int DEBUG_MESSAGE_PORT = getDebugMessagePort();

    private static int getMessagePort() {
        return 51462;
    }

    private static int getDebugMessagePort() {
        return 51463;
    }
}
