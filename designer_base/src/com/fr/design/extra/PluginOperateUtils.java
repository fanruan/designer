package com.fr.design.extra;

import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.design.extra.exe.callback.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginControllerHelper;
import com.fr.plugin.manage.control.PluginTask;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.view.PluginView;
import com.fr.stable.StringUtils;

import javax.swing.*;
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
            FRContext.getLogger().error(e.getMessage(), e);
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
                int rv = JOptionPane.showConfirmDialog(
                    null,
                    Inter.getLocText("FR-Plugin_Delete_Confirmed"),
                    Inter.getLocText("FR-Designer-Plugin_Warning"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                );
                if (rv == JOptionPane.OK_OPTION) {
                    PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
                    PluginManager.getController().uninstall(pluginMarker, isForce, new UninstallPluginCallback(pluginMarker, jsCallback));
                }
            }
        });
    }

    public static void readUpdateOnline(final JSCallback jsCallback) {
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PluginView> plugins = PluginsReaderFromStore.readPluginsForUpdate();
                    JSONArray jsonArray = new JSONArray();
                    for (PluginView plugin : plugins) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("pluginid", plugin.getID());
                        jsonArray.put(jsonObject);
                    }
                    String result = jsonArray.toString();
                    jsCallback.execute(result);
                } catch (Exception e) {
                    FRLogger.getLogger().error(e.getMessage());
                }
            }
        }).start();


    }

    public static void searchPlugin(final String keyword, final JSCallback jsCallback) {
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isBlank(keyword)) {
                        getRecommendPlugins(jsCallback);
                        return;
                    }
                    HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("shop.plugin.store") + "&keyword=" + keyword);
                    httpClient.asGet();
                    String result = httpClient.getResponseText();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONArray resultJSONArray = PluginUtils.filterPluginsFromVersion(jsonArray);
                    jsCallback.execute(resultJSONArray.toString());
                } catch (Exception e) {
                    FRLogger.getLogger().error(e.getMessage());
                }
            }
        }).start();

    }

    public static void getPluginFromStore(final String category, final String seller, final String fee, final JSCallback jsCallback) {
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                String plistUrl = SiteCenter.getInstance().acquireUrlByKind("shop.plugin.plist") + "?";
                boolean getRecommend = StringUtils.isEmpty(category) && StringUtils.isEmpty(seller) && StringUtils.isEmpty(fee);
                if (getRecommend) {
                    getRecommendPlugins(jsCallback);
                    return;
                }

                if (StringUtils.isNotBlank(plistUrl)) {
                    StringBuilder url = new StringBuilder();
                    url.append(plistUrl);
                    dealParams(url, category, seller, fee);
                    try {
                        HttpClient httpClient = new HttpClient(url.toString());
                        httpClient.asGet();
                        String result = httpClient.getResponseText();
                        JSONObject resultJSONObject = new JSONObject(result);
                        JSONArray resultArr = resultJSONObject.getJSONArray("result");
                        JSONArray resultJSONArray = PluginUtils.filterPluginsFromVersion(resultArr);
                        jsCallback.execute(resultJSONArray.toString());
                    } catch (Exception e) {
                        FRLogger.getLogger().error(e.getMessage());
                    }
                } else {
                    String result = PluginConstants.CONNECTION_404;
                    jsCallback.execute(result);
                }
            }
        
        }).start();

    }

    public static void getRecommendPlugins(JSCallback jsCallback) {
        String plistUrl = SiteCenter.getInstance().acquireUrlByKind("shop.plugin.feature");
        try {
            HttpClient httpClient = new HttpClient(plistUrl.toString());
            String result = httpClient.getResponseText();
            JSONArray jsonArray = new JSONArray(result);
            JSONArray resultJSONArray = PluginUtils.filterPluginsFromVersion(jsonArray);
            jsCallback.execute(resultJSONArray.toString());
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }

    }

    public static void dealParams(StringBuilder url, String category, String seller, String fee) {
        if (StringUtils.isNotBlank(category)) {
            url.append("cid=").append(category.split("-")[1]);
        } else {
            url.append("cid=").append("");
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
                    url.append("&seller=").append("");
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
                    url.append("&fee=").append("");
            }
        }
    }

    public static void getPluginCategories(final JSCallback jsCallback) {
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                String url = SiteCenter.getInstance().acquireUrlByKind("shop.plugin.category");
                if (url != null) {
                    HttpClient httpClient = new HttpClient(url);
                    result = httpClient.getResponseText();
                } else {
                    result = PluginConstants.CONNECTION_404;
                }
                jsCallback.execute(result);
            }
        }).start();
    }

    public static void getPluginPrefix(final JSCallback jsCallback) {
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = SiteCenter.getInstance().acquireUrlByKind("plugin.url.prefix");
                jsCallback.execute(result);
            }
        }).start();
    }

    public static void getLoginInfo(JSCallback jsCallback, UILabel uiLabel) {
        String username = ConfigManager.getProviderInstance().getBbsUsername();
        if (StringUtils.isEmpty(username)) {
            jsCallback.execute("");
            uiLabel.setText(Inter.getLocText("FR-Base_UnSignIn"));
        } else {
            uiLabel.setText(username);
            String result =  username;
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
            if(pluginInfo.length() != 0){
                pluginInfo.append("\n");
            }
            PluginTask pluginTask = pluginTaskResult.getCurrentTask();
            if(pluginTask == null){
                pluginInfo.append(PluginUtils.getMessageByErrorCode(pluginTaskResult.errorCode()));
                continue;
            }
            PluginMarker pluginMarker = pluginTask.getToMarker();
            PluginContext pluginContext = PluginManager.getContext(pluginMarker);
            if (pluginContext != null) {
                pluginInfo.append(pluginContext.getName()).append(PluginUtils.getMessageByErrorCode(pluginTaskResult.errorCode()));
            }else{
                pluginInfo.append(pluginMarker.getPluginID()).append(PluginUtils.getMessageByErrorCode(pluginTaskResult.errorCode()));
            }
        }
        return pluginInfo.toString();
    }



}
