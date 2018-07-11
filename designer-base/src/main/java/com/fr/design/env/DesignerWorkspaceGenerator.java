package com.fr.design.env;

import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.AuthException;
import com.fr.workspace.connect.WorkspaceClient;

/**
 * 根据配置生成运行环境
 */
public class DesignerWorkspaceGenerator {

    public static Workspace generate(DesignerWorkspaceInfo config) throws AuthException {

        if (config == null || config.getType() == null) {
            return null;
        }

        Workspace workspace = null;
        switch (config.getType()) {
            case Local: {
                workspace = WorkContext.getFactory().build(config.getPath());
                break;
            }
            case Remote: {
                WorkspaceClient client = WorkContext.getConnector().connect(config.getConnection());
                if (client != null) {
                    workspace = new RemoteWorkspace(client, config.getConnection());
                }
                break;
            }
        }
        return workspace;
    }
}
