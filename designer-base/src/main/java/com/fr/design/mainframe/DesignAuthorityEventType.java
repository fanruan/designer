package com.fr.design.mainframe;

import com.fr.event.Event;

public enum  DesignAuthorityEventType implements Event<DesignerFrame> {

    // 退 出权限编辑
    StartEdit,
    // 进入权限编辑
    StopEdit;
}
