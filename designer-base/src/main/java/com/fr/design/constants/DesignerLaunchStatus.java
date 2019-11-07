package com.fr.design.constants;

import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Null;

/**
 * 设计器启动事件类型
 *
 * @author vito
 * @date 2019-06-18
 */
public enum DesignerLaunchStatus implements Event<Null> {
    /**
     * 初始化环境完成
     */
    WORKSPACE_INIT_COMPLETE,

    /**
     * 设计器模块启动完成
     */
    DESIGNER_INIT_COMPLETE,

    /**
     * 打开模板完成
     */
    OPEN_LAST_FILE_COMPLETE,

    /**
     * 启动完成
     */
    STARTUP_COMPLETE;

    private static DesignerLaunchStatus status;

    public static DesignerLaunchStatus getStatus() {
        return status;
    }

    public static void setStatus(DesignerLaunchStatus state) {
        status = state;
        EventDispatcher.fire(DesignerLaunchStatus.getStatus());
    }
}
