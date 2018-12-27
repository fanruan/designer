package com.fr.env;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.AssistUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.channel.http.FunctionalHttpRequest;

/**
 * 测试连接的结果。
 * 不改变原有逻辑的情况下，加入一层转化。
 * 根据这里的转化结果，判断需要提示哪些内容。
 *
 * created by Harrison on 2018/12/20
 **/
public enum TestConnectionResult {
    /**
     * 完全成功, 版本匹配，测试连接成功。
     */
    Fully_Success,

    /**
     * 不完全成功，版本不匹配，但测试连接成功。
     */
    Partly_Sucess,

    /**
     * 完全失败，直接没连上
     */
    Fully_Failed,

    /**
     * 验证 Token 失败
     */
    Auth_Failed;

    public static TestConnectionResult parse(Boolean value, WorkspaceConnectionInfo info) {
        if (value == null) {
            return Auth_Failed;
        }

        if (!value) {
            return Fully_Failed;
        }
        try {

            String serverVersion = new FunctionalHttpRequest(info).getServerVersion();
            if (AssistUtils.equals(serverVersion, WorkContext.getVersion())) {
                return Fully_Success;
            }

            return Partly_Sucess;
        } catch (Exception e) {

            // 发生异常，说明没连接上。返回完全失败。
            FineLoggerFactory.getLogger().error(e.getMessage());
            return Fully_Failed;
        }


    }
}
