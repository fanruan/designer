package com.fr.env;


import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.env.EnvContext;


public class SignIn {
    public static Env lastSelectedEnv;// 记录最后登录的Env

    /**
     * 注册入环境
     * @param selectedEnv 选择的环境
     * @throws Exception 异常
     */
    public static void signIn(Env selectedEnv) throws Exception {
        boolean validServer;
        signOutOldEnv(selectedEnv);
        selectedEnv.signIn();
        validServer = true;
        if (validServer) {
            DesignUtils.switchToEnv(selectedEnv);
            lastSelectedEnv = selectedEnv;
        }
    }

    private static void signOutOldEnv(Env newEnv) {
        // 环境相同直接返回，避免浪费过多时间
        if (lastSelectedEnv == null || ComparatorUtils.equals(lastSelectedEnv, newEnv)) {
            return;
        }
        try {
            EnvContext.fireBeforeSignOut();
            lastSelectedEnv.signOut();
            EnvContext.fireAfterSignOut();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }
}
