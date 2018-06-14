package com.fr.design.env;

import com.fr.base.Env;
import com.fr.base.env.EnvConfig;
import com.fr.base.env.LocalEnvConfig;
import com.fr.env.RemoteEnv;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceClient;
import com.fr.workspace.connect.WorkspaceConnection;

/**
 * 根据配置生成运行环境
 */
public class DesignerWorkspaceGenerator {
    
    public static Workspace generate(EnvConfig config) {
        
        Workspace workspace = null;
        if (config instanceof LocalEnvConfig) {
            workspace = WorkContext.getFactory().build(config.getPath());
        } else if (config instanceof RemoteEnvConfig) {
            RemoteEnvConfig remoteConfig = (RemoteEnvConfig) config;
            WorkspaceClient client = WorkContext.getConnector().connect(new WorkspaceConnection(remoteConfig.getHost(), remoteConfig.getPort(), remoteConfig.getUsername(), remoteConfig.getPassword()));
            workspace = new RemoteWorkspace(client, remoteConfig.getHost() + ":" + remoteConfig.getPort(), remoteConfig.getPassword());
        }
        return workspace;
    }
}
