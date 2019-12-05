package com.fr.design.extra;

import com.fr.config.MarketConfig;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.exe.callback.InstallFromDiskCallback;
import com.fr.design.extra.exe.callback.InstallOnlineCallback;
import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.extra.exe.callback.ModifyStatusCallback;
import com.fr.design.extra.exe.callback.UninstallPluginCallback;
import com.fr.design.extra.exe.callback.UpdateFromDiskCallback;
import com.fr.design.extra.exe.callback.UpdateOnlineCallback;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginControllerHelper;
import com.fr.plugin.manage.control.PluginTask;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.view.PluginView;
import com.fr.stable.StringUtils;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.File;
import java.util.List;


/**
 * Created by ibm on 2017/5/26.
 */
public class PluginOperateUtils {

    public static void installPluginOnline(final PluginMarker pluginMarker, JSCallback jsCallback) {
        //下载插件
        PluginTask pluginTask = PluginTask.installTask(pluginMarker);
        PluginControllerHelper.installOnline(pluginMarker, new InstallOnlineCallback(pluginTask, jsCallback));
    }

    public static void installPluginFromDisk(File zipFile, JSCallback jsCallback) {
        PluginManager.getController().install(zipFile, new InstallFromDiskCallback(zipFile, jsCallback));
    }


    public static void updatePluginOnline(List<PluginMarker> pluginMarkerList, JSCallback jsCallback) {
        for (int i = 0; i < pluginMarkerList.size(); i++) {
            updatePluginOnline(pluginMarkerList.get(i), jsCallback);
        }
    }

    public static void updatePluginOnline(PluginMarker pluginMarker, JSCallback jsCallback) {
        try {
            JSONObject latestPluginInfo = PluginUtils.getLatestPluginInfo(pluginMarker.getPluginID());
            String latestPluginVersion = (String) latestPluginInfo.get("version");
            PluginMarker toPluginMarker = PluginMarker.create(pluginMarker.getPluginID(), latestPluginVersion);
            //当前已经安装的相同ID插件marker
            PluginMarker currentMarker = PluginUtils.getInstalledPluginMarkerByID(pluginMarker.getPluginID());
            PluginTask pluginTask = PluginTask.updateTask(currentMarker, toPluginMarker);
            PluginControllerHelper.updateOnline(currentMarker, toPluginMarker, new UpdateOnlineCallback(pluginTask, jsCallback));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

    }


    public static void updatePluginFromDisk(File zipFile, JSCallback jsCallback) {
        PluginManager.getController().update(zipFile, new UpdateFromDiskCallback(zipFile, jsCallback));
    }


    public static void setPluginActive(String pluginInfo, JSCallback jsCallback) {
        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
        PluginContext plugin = PluginManager.getContext(pluginMarker);
        boolean active = plugin.isActive();
        PluginTaskCallback modifyStatusCallback = new ModifyStatusCallback(active, jsCallback);
        if (active) {
            PluginManager.getController().forbid(pluginMarker, modifyStatusCallback);
        } else {
            PluginManager.getController().enable(pluginMarker, modifyStatusCallback);
        }
    }

    public static void uninstallPlugin(final String pluginInfo, final boolean isForce, final JSCallback jsCallback) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int rv = FineJOptionPane.showConfirmDialog(
                        null,
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Delete_Confirmed"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (rv == JOptionPane.OK_OPTION) {
                    PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
                    PluginManager.getController().uninstall(pluginMarker, isForce, new UninstallPluginCallback(pluginMarker, jsCallback));
                }
            }
        });
    }

    public static String getRecommendPlugins() {
        String plistUrl = CloudCenter.getInstance().acquireUrlByKind("shop.plugin.feature");
        JSONArray resultArray = JSONArray.create();
        try {
            HttpClient httpClient = new HttpClient(plistUrl);
            String result = httpClient.getResponseText();
            JSONArray jsonArray = new JSONArray(result);
            resultArray = PluginUtils.filterPluginsFromVersion(jsonArray);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return resultArray.toString();
    }

    public static void dealParams(StringBuilder url, String category, String seller, String fee, String scope) {
        if (StringUtils.isNotBlank(category)) {
            url.append("cid=").append(category.split("-")[1]);
        } else {
            url.append("cid=").append(StringUtils.EMPTY);
        }
        if (StringUtils.isNotBlank(seller)) {
            switch (seller.split("-")[1]) {
                case "finereport":
                    url.append("&seller=").append(1);
                    break;
                case "developer":
                    url.append("&seller=").append(2);
                    break;
                default:
                    url.append("&seller=").append(StringUtils.EMPTY);
            }
        }
        if (StringUtils.isNotBlank(fee)) {
            switch (fee.split("-")[1]) {
                case "free":
                    url.append("&fee=").append(1);
                    break;
                case "charge":
                    url.append("&fee=").append(2);
                    break;
                default:
                    url.append("&fee=").append(StringUtils.EMPTY);
            }
        }
        if (StringUtils.isNotBlank(scope)) {
            switch (scope.split("-")[1]) {
                case "universal":
                    url.append("&scope=").append(1);
                    break;
                case "program":
                    url.append("&scope=").append(2);
                    break;
                default:
                    url.append("&scope=").append(StringUtils.EMPTY);
            }
        }
    }

    public static void getLoginInfo(JSCallback jsCallback, UILabel uiLabel) {
        String username = MarketConfig.getInstance().getBbsUsername();
        if (StringUtils.isEmpty(username)) {
            jsCallback.execute(StringUtils.EMPTY);
            uiLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_UnSignIn"));
        } else {
            uiLabel.setText(username);
            String result = username;
            jsCallback.execute(result);
        }
    }

    public static boolean pluginValidate(PluginView pluginView) {
        return StringUtils.isNotEmpty(pluginView.getID())
                && StringUtils.isNotEmpty(pluginView.getName())
                && StringUtils.isNotEmpty(pluginView.getVersion())
                && StringUtils.isNotEmpty(pluginView.getEnvVersion());
    }

    public static String getSuccessInfo(PluginTaskResult result) {
        StringBuilder pluginInfo = new StringBuilder();
        List<PluginTaskResult> pluginTaskResults = result.asList();
        for (PluginTaskResult pluginTaskResult : pluginTaskResults) {
            if (pluginInfo.length() != 0) {
                pluginInfo.append("\n");
            }
            PluginTask pluginTask = pluginTaskResult.getCurrentTask();
            if (pluginTask == null) {
                pluginInfo.append(PluginUtils.getMessageByErrorCode(pluginTaskResult.errorCode()));
                continue;
            }
            PluginMarker pluginMarker = pluginTask.getToMarker();
            PluginContext pluginContext = PluginManager.getContext(pluginMarker);
            if (pluginContext != null) {
                pluginInfo.append(pluginContext.getName()).append(PluginUtils.getMessageByErrorCode(pluginTaskResult.errorCode()));
            } else {
                pluginInfo.append(pluginMarker.getPluginID()).append(PluginUtils.getMessageByErrorCode(pluginTaskResult.errorCode()));
            }
        }
        return pluginInfo.toString();
    }

    public static String getSwitchedInfo(PluginTaskResult result) {
        StringBuilder pluginInfo = new StringBuilder();
        List<PluginTaskResult> pluginTaskResults = result.asList();
        for (PluginTaskResult pluginTaskResult : pluginTaskResults) {
            PluginTask pluginTask = pluginTaskResult.getCurrentTask();
            if (pluginTask == null) {
                continue;
            }
            PluginMarker pluginMarker = pluginTask.getToMarker();
            PluginContext pluginContext = PluginManager.getContext(pluginMarker);
            if (pluginContext != null && pluginContext.getSelfState() == 1) {
                pluginInfo.append("\n").append(pluginContext.getSwitchedReason());
            }
        }
        return pluginInfo.toString();
    }

}
