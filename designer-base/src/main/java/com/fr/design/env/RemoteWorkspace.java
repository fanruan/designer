package com.fr.design.env;

import com.fr.locale.InterProviderFactory;
import com.fr.stable.AssistUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceClient;
import com.fr.workspace.connect.WorkspaceConnection;
import com.fr.workspace.server.authority.decision.DecisionOperator;

/**
 * Created by juhaoyu on 2018/6/14.
 * 远程工作目录
 */
public class RemoteWorkspace implements Workspace {

    private final WorkspaceClient client;

    private final String address;
    
    private final WorkspaceConnection connection;
    
    RemoteWorkspace(WorkspaceClient client, WorkspaceConnection connection) {

        this.client = client;
        this.address = connection.getUrl();
        this.connection = connection;
    }

    @Override
    public String getPath() {

        return address;
    }
    
    @Override
    public String getDescription() {
        
        return InterProviderFactory.getProvider().getLocText("Fine-Designer_Basic_Remote_Env");
    }
    
    @Override
    public boolean isWarDeploy() {

        return false;
    }

    @Override
    public boolean isLocal() {

        return false;
    }

    @Override
    public boolean isRoot() {
        try {
            return WorkContext.getCurrent().get(DecisionOperator.class).isRoot(WorkContext.getConnector().currentUser());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public <T> T get(Class<T> type) {

        return client.getPool().get(type);
    }
    
    @Override
    public int hashCode() {
        
        return connection.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        
        return obj != null && obj instanceof RemoteWorkspace && AssistUtils.equals(((RemoteWorkspace) obj).connection, this.connection);
    }
}
