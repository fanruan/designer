package com.fr.design.env;

import com.fr.stable.xml.XMLable;
import com.fr.workspace.connect.WorkspaceConnection;

public interface DesignerWorkspaceInfo extends XMLable {
    DesignerWorkspaceType getType();

    String getName();

    String getPath();

    WorkspaceConnection getConnection();
}
