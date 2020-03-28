package com.fr.env;

import com.fr.design.i18n.Toolkit;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.AssistUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.channel.http.FunctionalHttpRequest;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * 测试连接的结果。
 * 不改变原有逻辑的情况下，加入一层转化。
 * 根据这里的转化结果，判断需要提示哪些内容。
 * <p>
 * created by Harrison on 2018/12/20
 **/
public enum TestConnectionResult {
    /**
     * 完全成功, 版本匹配，测试连接成功。
     */
    FULLY_SUCCESS {
        @Override
        public Icon getIcon() {
            return UIManager.getIcon("OptionPane.informationIcon");
        }

        @Override
        public String getText() {
            return Toolkit.i18nText("Fine-Design_Basic_Remote_Connect_Successful");
        }
    },

    /**
     * 不完全成功，版本不匹配，但测试连接成功。该状态先保留
     */
    PARTLY_SUCCESS {
        @Override
        public Icon getIcon() {
            return UIManager.getIcon("OptionPane.warningIcon");
        }

        @Override
        public String getText() {
            return Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Version_Inconsistence_Test");
        }
    },

    /**
     * 完全失败，直接没连上
     */
    FULLY_FAILED {
        @Override
        public Icon getIcon() {
            return UIManager.getIcon("OptionPane.errorIcon");
        }

        @Override
        public String getText() {
            return Toolkit.i18nText("Fine-Design_Basic_Remote_Connect_Failed");
        }
    },

    /**
     * 验证 Token 失败
     */
    AUTH_FAILED {
        @Override
        public Icon getIcon() {
            return UIManager.getIcon("OptionPane.errorIcon");
        }

        @Override
        public String getText() {
            return Toolkit.i18nText("Fine-Design_Basic_Remote_Connect_Auth_Failed");
        }
    };

    public abstract Icon getIcon();

    public abstract String getText();

    public static TestConnectionResult parse(Boolean value, WorkspaceConnectionInfo info) {
        if (value == null) {
            return AUTH_FAILED;
        }
        if (!value) {
            return FULLY_FAILED;
        }
        //去掉测试连接时所做的检测
        return FULLY_SUCCESS;
    }
}
