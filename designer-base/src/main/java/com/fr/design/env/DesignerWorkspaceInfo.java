package com.fr.design.env;

import com.fr.stable.xml.XMLable;
import com.fr.workspace.connect.WorkspaceConnectionInfo;

public interface DesignerWorkspaceInfo extends XMLable {
    DesignerWorkspaceType getType();

    String getName();

    String getPath();

    //获取提醒时间，用于判断是否做服务检测
    String getRemindTime();

    WorkspaceConnectionInfo getConnection();

    boolean checkValid();
}
