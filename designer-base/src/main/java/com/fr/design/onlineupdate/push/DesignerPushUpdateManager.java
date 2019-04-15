package com.fr.design.onlineupdate.push;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

/**
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateManager {
    private static final String SPLIT_CHAR = "-";
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

    private void initUpdateInfo(String currentVersion, String latestVersion) {
        String lastIgnoredVersion = config.getLastIgnoredVersion();
        String updatePushInfo = CloudCenter.getInstance().acquireUrlByKind("update.push");
        JSONObject pushData = new JSONObject(updatePushInfo);

        updateInfo = new DesignerUpdateInfo(currentVersion, latestVersion, lastIgnoredVersion, pushData);
    }

    private String getFullLatestVersion() {
        HttpClient hc = new HttpClient(CloudCenter.getInstance().acquireUrlByKind("jar10.update"));
        return new JSONObject(hc.getResponseText()).optString("buildNO");
    }

    private String getVersionByFullNO(String fullNO) {
        if (fullNO.contains(SPLIT_CHAR)) {
            fullNO = fullNO.substring(fullNO.lastIndexOf(SPLIT_CHAR) + 1);
        }
        return fullNO;
    }

    private String getPrefixByFullNO(String fullNO) {
        if (fullNO.contains(SPLIT_CHAR)) {
            return fullNO.substring(0, fullNO.lastIndexOf(SPLIT_CHAR));
        }
        return StringUtils.EMPTY;
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
    public void checkAndPop() {
        new Thread() {
            @Override
            public void run() {
                if (!shouldPopUp()) {
                    FineLoggerFactory.getLogger().debug("skip push update");
                    return;
                }
                // todo: do pop
                final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
                DesignerPushUpdateDialog.createAndShow(designerFrame, updateInfo);
            }
        }.start();
    }

    private boolean shouldPopUp() {
        if (updateInfo == null) {
            String fullCurrentVersion = GeneralUtils.readFullBuildNO();
            // todo: 开发测试用
            if (!fullCurrentVersion.contains(SPLIT_CHAR)) {
                fullCurrentVersion = "stable-2019.01.03.17.01.05.257";
            }


            String fullLatestVersion = getFullLatestVersion();
            boolean isValidJarVersion = isValidJarVersion(fullCurrentVersion, fullLatestVersion);
            if (!isValidJarVersion) {
                FineLoggerFactory.getLogger().info("Jar version is not valid for push update.");
                return false;
            } else {
                String currentVersion = getVersionByFullNO(fullCurrentVersion);
                String latestVersion = getVersionByFullNO(fullLatestVersion);
                initUpdateInfo(currentVersion, latestVersion);
            }
        }

        return isAutoPushUpdateSupported() && updateInfo.hasNewPushVersion();
    }

    private boolean isValidJarVersion(String fullCurrentVersion, String fullLatestVersion) {
        // todo: 目前设定的逻辑是 feature/release/stable 都弹，且不区分版本号。后期肯定要变的，注释代码先留着
//        // 无效的情况：
//        // 1. 版本号格式有误
//        // 2. 当前用的是 release 或 feature 的 jar 包
//        // 3. 代码启动的
//        String prefix = getPrefixByFullNO(fullLatestVersion);
//        return StringUtils.isNotEmpty(prefix) && fullCurrentVersion.startsWith(prefix);

        // 无效的情况：
        // 1. 版本号格式有误（正常情况下都有前缀，只有异常的时候才可能出现）
        // 2. 代码启动的（fullCurrentVersion 为"不是安装版本"）
        String prefix = getPrefixByFullNO(fullLatestVersion);
        return StringUtils.isNotEmpty(prefix) && fullCurrentVersion.contains(SPLIT_CHAR);
    }

    /**
     * 跳转到更新升级窗口，并自动开始更新
     */
    void doUpdate() {
        // todo
    }

    /**
     * 跳过当前的推送版本
     */
    void skipCurrentPushVersion() {
        // todo
    }
}
