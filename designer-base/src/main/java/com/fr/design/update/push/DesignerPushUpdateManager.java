package com.fr.design.update.push;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.event.DesignerOpenedListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.os.impl.SupportOSImpl;
import com.fr.design.update.ui.dialog.UpdateMainDialog;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateManager {
    private static final String SPLIT_CHAR = "-";
    private static DesignerPushUpdateManager singleton;
    private final ExecutorService checkUpdateService = Executors.newSingleThreadExecutor(new NamedThreadFactory("DesignerCheckUpdate"));
    private final ExecutorService updateService = Executors.newSingleThreadExecutor(new NamedThreadFactory("DesignerUpdate"));

    private DesignerUpdateInfo updateInfo;

    private DesignerPushUpdateManager() {
    }

    public static DesignerPushUpdateManager getInstance() {
        if (singleton == null) {
            singleton = new DesignerPushUpdateManager();
        }
        return singleton;
    }

    public void preparePushUpdate() {
        if (DesignerPushUpdateConfigManager.getInstance().isAutoPushUpdateEnabled()) {
            DesignerContext.getDesignerFrame().addDesignerOpenedListener(new DesignerOpenedListener() {
                @Override
                public void designerOpened() {
                    getInstance().checkAndPop();
                }
            });
        }
    }

    private void initUpdateInfo(String currentVersion, String latestVersion) {
        String lastIgnoredVersion = DesignerPushUpdateConfigManager.getInstance().getLastIgnoredVersion();
        String updatePushInfo = CloudCenter.getInstance().acquireUrlByKind("update.push");
        JSONObject pushData = new JSONObject(updatePushInfo);

        updateInfo = new DesignerUpdateInfo(currentVersion, latestVersion, lastIgnoredVersion, pushData);
    }

    private String getFullLatestVersion() {
        try {
            String res = HttpToolbox.get(CloudCenter.getInstance().acquireUrlByKind("jar10.update"));
            return new JSONObject(res).optString("buildNO");
        } catch (Throwable e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
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
     * 检查更新，如果有合适的更新版本，则弹窗
     */
    private void checkAndPop() {
        checkUpdateService.execute(new Runnable() {
            @Override
            public void run() {
                if (!shouldPopUp()) {
                    FineLoggerFactory.getLogger().debug("skip push update");
                    return;
                }
                final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
                DesignerPushUpdateDialog.createAndShow(designerFrame, updateInfo);
            }
        });
        checkUpdateService.shutdown();
    }

    private boolean shouldPopUp() {
        if (updateInfo == null) {
            String fullCurrentVersion = GeneralUtils.readFullBuildNO();

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
        return SupportOSImpl.AUTOPUSHUPDATE.support() && updateInfo.hasNewPushVersion();
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
        updateService.execute(new Runnable() {
            @Override
            public void run() {
                UpdateMainDialog dialog = new UpdateMainDialog(DesignerContext.getDesignerFrame());
                dialog.setAutoUpdateAfterInit();
                dialog.showDialog();
            }
        });
        updateService.shutdown();
    }

    /**
     * 跳过当前的推送版本
     */
    void skipCurrentPushVersion() {
        if (updateInfo == null) {
            return;
        }
        DesignerPushUpdateConfigManager.getInstance().setLastIgnoredVersion(updateInfo.getPushVersion());
    }
}
