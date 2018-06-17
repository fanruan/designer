package com.fr.design.env;

import com.fr.general.Inter;
import com.fr.report.util.RemoteDesignAuthenticateUtils;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceClient;
import com.fr.workspace.connect.WorkspaceConnection;
import com.fr.workspace.server.WorkspaceServerContext;

/**
 * Created by juhaoyu on 2018/6/14.
 * 远程工作目录
 */
public class RemoteWorkspace implements Workspace {

    private final WorkspaceClient client;

    private final String address;

    private final String userName;

    RemoteWorkspace(WorkspaceClient client, WorkspaceConnection connection) {

        this.client = client;
        this.address = connection.getUrl();
        this.userName = connection.getUserName();
    }

    @Override
    public String getName() {

        return userName;
    }

    @Override
    public String getPath() {

        return address;
    }

    @Override
    public String getDescription() {

        return userName + "@" + "[" + Inter.getLocText("Fine-Designer_Basic_Remote_Env") + "]";
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
            return RemoteDesignAuthenticateUtils.isRoot(WorkspaceServerContext.currentUsername());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public <T> T get(Class<T> type) {

        return client.getPool().get(type);
    }
}
