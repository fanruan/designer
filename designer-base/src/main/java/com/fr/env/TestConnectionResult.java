package com.fr.env;

import com.fr.stable.AssistUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.channel.http.FunctionalHttpRequest;

/**
 * created by Harrison on 2018/12/20
 **/
public enum TestConnectionResult {
    /**
     * 完全成功, 版本匹配，测试连接成功。
     */
    Fully_Success(0),

    /**
     * 不完全成功，版本不匹配，但测试连接成功。
     */
    Partly_Sucess(1),

    /**
     * 完全失败，直接没连上
     */
    Fully_Failed(2),

    /**
     * 验证 Token 失败
     */
    Auth_Failed(3);

    private int sign;

    TestConnectionResult(int i) {
        this.sign = i;
    }

    public static TestConnectionResult parse(Boolean value, WorkspaceConnectionInfo info) throws Exception {
        if (value == null) {
            return Auth_Failed;
        }

        if (!value) {
            return Fully_Failed;
        }

        String serverVersion = new FunctionalHttpRequest(info).getServerVersion();

        if (AssistUtils.equals(serverVersion, WorkContext.getVersion())) {
            return Fully_Success;
        }

        return Partly_Sucess;
    }
}
