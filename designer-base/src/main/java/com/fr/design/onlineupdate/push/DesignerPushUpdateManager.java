package com.fr.design.onlineupdate.push;

import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.workspace.WorkContext;

/**
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateManager {
    private static DesignerPushUpdateManager singleton;
    private DesignerUpdateInfo updateInfo;
    private DesignerPushUpdateConfigManager config;

    private DesignerPushUpdateManager() {
        config = DesignerPushUpdateConfigManager.getInstance();
    }

    public static DesignerPushUpdateManager getInstance() {
        if (singleton == null) {
            singleton = new DesignerPushUpdateManager();
        }
        return singleton;
    }

    private void initUpdateInfo() {
        String currentVersion = GeneralUtils.readFullBuildNO();

        // todo：耗时请求，可能需要优化
        HttpClient hc = new HttpClient(CloudCenter.getInstance().acquireUrlByKind("jar10.update"));
        String latestVersion = new JSONObject(hc.getResponseText()).optString("versionNO");

        String updatePushInfo = CloudCenter.getInstance().acquireUrlByKind("update.push");
        JSONObject pushData = new JSONObject(updatePushInfo);

        updateInfo = new DesignerUpdateInfo(currentVersion, latestVersion, pushData);
    }

    /**
     * "自动更新推送"选项是否生效
     */
    public boolean isAutoPushUpdateSupported() {
        boolean isLocalEnv = WorkContext.getCurrent().isLocal();
        boolean isChineseEnv = GeneralContext.isChineseEnv();

        return isAutoPushUpdateSupported(isLocalEnv, isChineseEnv);
    }

    private boolean isAutoPushUpdateSupported(boolean isLocalEnv, boolean isChineseEnv) {
        // 远程设计和非中文环境，都不生效
        return isLocalEnv && isChineseEnv;
    }

    /**
     * 检查更新，如果有合适的更新版本，则弹窗
     */
    public void popUpDialog() {
        if (!shouldPopUp()) {
            return;
        }
        // todo: do pop
    }

    private boolean shouldPopUp() {
        if (updateInfo == null) {
            initUpdateInfo();
        }

        return isAutoPushUpdateSupported() && updateInfo.hasNewPushVersion(config.getLastIgnoredVersion());
    }
}
