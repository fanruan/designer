package com.fr.design.env;

import com.fr.cluster.engine.base.FineClusterConfig;
import com.fr.design.i18n.Toolkit;
import com.fr.base.operator.common.CommonOperator;
import com.fr.rpc.ExceptionHandler;
import com.fr.rpc.RPCInvokerExceptionInfo;
import com.fr.stable.AssistUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceClient;
import com.fr.workspace.connect.WorkspaceConnection;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.server.authority.decision.DecisionOperator;
import com.fr.workspace.engine.rpc.WorkspaceProxyPool;
import com.fr.workspace.pool.WorkObjectPool;

/**
 * Created by juhaoyu on 2018/6/14.
 * 远程工作目录
 */
public class RemoteWorkspace implements Workspace {

    private final WorkspaceClient client;

    private final String address;
    
    private final WorkspaceConnectionInfo connection;
    
    private volatile Boolean isRoot = null;
    
    RemoteWorkspace(WorkspaceClient client, WorkspaceConnectionInfo connection) {

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
        
        return Toolkit.i18nText("Fine-Design_Basic_Remote_Env");
    }
    
    @Override
    public boolean isWarDeploy() {
        return WorkContext.getCurrent().get(CommonOperator.class, new ExceptionHandler<Boolean>() {
            @Override
            public Boolean callHandler(RPCInvokerExceptionInfo rpcInvokerExceptionInfo) {
                return false;
            }
         }).isWarDeploy();
    }

    @Override
    public boolean isLocal() {

        return false;
    }

    @Override
    public boolean isRoot() {
    
        if (isRoot == null) {
            synchronized (this) {
                if (isRoot == null) {
                    isRoot = WorkContext.getCurrent().get(DecisionOperator.class).isRoot(getConnection().getUserName());
                }
            }
        }
        return isRoot;
    }

    @Override
    public boolean isCluster() {
        return FineClusterConfig.getInstance().isCluster();
    }

    @Override
    public WorkspaceConnection getConnection() {
        
        return client.getConnection();
    }
    
    @Override
    public <T> T get(Class<T> type) {

        return client.getPool().get(type);
    }

    @Override
    public <T> T get(Class<T> type, ExceptionHandler exceptionHandler){
        if(exceptionHandler != null) {
            WorkObjectPool objectPool = client.getPool();
            if (objectPool instanceof WorkspaceProxyPool) {
                return ((WorkspaceProxyPool) objectPool).get(type, exceptionHandler);
            }else {
                return client.getPool().get(type);
            }
        }
        return client.getPool().get(type);
    }
    
    @Override
    public void close() {
        
        client.close();
    }
    
    @Override
    public int hashCode() {
        
        return connection.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        
        return obj instanceof RemoteWorkspace && AssistUtils.equals(((RemoteWorkspace) obj).connection, this.connection);
    }

    public WorkspaceClient getClient(){
        return client;
    }
}
