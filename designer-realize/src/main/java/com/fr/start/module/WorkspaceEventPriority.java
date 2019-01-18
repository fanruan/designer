package com.fr.start.module;

/**
 * 切换环境事件优先级暂时方案
 * todo 看是不是需要使用另外的类型处理优先级
 */
public final class WorkspaceEventPriority {

    private WorkspaceEventPriority() {
    }

    public static final int MAX = 999;
    public static final int MID = 0;
    public static final int MIN = -999;

}
