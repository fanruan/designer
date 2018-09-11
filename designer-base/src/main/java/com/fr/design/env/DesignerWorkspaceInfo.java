package com.fr.design.env;

import com.fr.stable.xml.XMLable;
import com.fr.workspace.connect.WorkspaceConnectionInfo;

public interface DesignerWorkspaceInfo extends XMLable {
    DesignerWorkspaceType getType();

    String getName();

    String getPath();

    WorkspaceConnectionInfo getConnection();

    boolean checkValid();
}
