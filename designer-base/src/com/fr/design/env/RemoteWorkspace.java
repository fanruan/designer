package com.fr.design.env;

import com.fr.general.Inter;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceClient;

/**
 * Created by juhaoyu on 2018/6/14.
 * 远程工作目录
 */
public class RemoteWorkspace implements Workspace {
    
    private final WorkspaceClient client;
    
    private final String address;
    
    private final String userName;
    
    public RemoteWorkspace(WorkspaceClient client, String address, String userName) {
        
        this.client = client;
        this.address = address;
        this.userName = userName;
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
    public <T> T get(Class<T> type) {
        
        return client.getPool().get(type);
    }
}
