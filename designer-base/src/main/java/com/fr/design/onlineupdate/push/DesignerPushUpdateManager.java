package com.fr.design.onlineupdate.push;

import com.fr.general.GeneralContext;
import com.fr.workspace.WorkContext;

/**
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateManager {
    private static DesignerPushUpdateManager singleton;
//    private DesignerUpdateInfo updateInfo;

    private DesignerPushUpdateManager() {

    }

    public static DesignerPushUpdateManager getInstance() {
        if (singleton == null) {
            singleton = new DesignerPushUpdateManager();
        }
        return singleton;
    }

    /**
     * "自动更新推送"选项是否生效
     */
    public boolean isAutoPushUpdateSupported() {
        // 远程设计和非中文环境，都不生效
        return WorkContext.getCurrent().isLocal() && GeneralContext.isChineseEnv();
    }
    /**
     * 检查更新，如果有合适的更新版本，则弹窗
     */
//    public void checkAndPop() {
//        updateInfo.
//    }


}
