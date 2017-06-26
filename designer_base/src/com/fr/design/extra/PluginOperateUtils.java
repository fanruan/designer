package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.extra.exe.callback.*;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.bbs.BBSPluginLogin;
import com.fr.plugin.manage.bbs.BBSUserInfo;
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
        if (!BBSPluginLogin.getInstance().hasLogin()) {
            LoginCheckContext.fireLoginCheckListener();
        }
        if (BBSPluginLogin.getInstance().hasLogin()) {
            PluginTask pluginTask = PluginTask.installTask(pluginMarker);
            PluginControllerHelper.installOnline(pluginMarker, new InstallOnlineCallback(pluginTask, jsCallback));
        }
    }

    public static void installPluginFromDisk(File zipFile, JSCallback jsCallback) {
        PluginManager.getController().install(zipFile, new InstallFromDiskCallback(zipFile, jsCallback));
    }


    public static void updatePluginOnline(List<PluginMarker> pluginMarkerList, JSCallback jsCallback) {
        if (!(BBSPluginLogin.getInstance().hasLogin())) {
            LoginCheckContext.fireLoginCheckListener();
        }
        if (BBSPluginLogin.getInstance().hasLogin()) {
            for (int i = 0; i < pluginMarkerList.size(); i++) {
                updatePluginOnline(pluginMarkerList.get(i), jsCallback);
            }
        }
    }

    public static void updatePluginOnline(PluginMarker pluginMarker, JSCallback jsCallback) {
        try {
            JSONObject latestPluginInfo = PluginUtils.getLatestPluginInfo(pluginMarker.getPluginID());
            String latestPluginVersion = (String) latestPluginInfo.get("version");
            PluginMarker toPluginMarker = PluginMarker.create(pluginMarker.getPluginID(), latestPluginVersion);
            PluginTask pluginTask = PluginTask.updateTask(pluginMarker, toPluginMarker);
            PluginControllerHelper.updateOnline(pluginMarker, toPluginMarker, new UpdateOnlineCallback(pluginTask, jsCallback));
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

    }


    public static void updatePluginFromDisk(final String filePath, JSCallback jsCallback) {
        PluginManager.getController().update(new File(filePath), new UpdateFromDiskCallback(new File(filePath), jsCallback));
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
                    Inter.getLocText("FR-Designer-Plugin_Delete_Confirmed"),
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
                    jsCallback.execute(jsonArray.toString());
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
                        jsCallback.execute(resultArr.toString());
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
            jsCallback.execute(result);
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

    public static void getLoginInfo(JSCallback jsCallback) {

        if (!BBSPluginLogin.getInstance().hasLogin()) {
            String userName = DesignerEnvManager.getEnvManager().getBBSName();
            String password = DesignerEnvManager.getEnvManager().getBBSPassword();
            if (StringUtils.isNotBlank(userName)) {
                BBSPluginLogin.getInstance().login(new BBSUserInfo(userName, password));
            }
        }
        BBSUserInfo bbsUserInfo = BBSPluginLogin.getInstance().getUserInfo();
        String username = bbsUserInfo == null ? "" : bbsUserInfo.getUserName();
        String inShowUsername = DesignerEnvManager.getEnvManager().getInShowBBsName();
        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(inShowUsername)) {
            return;
        } else {
            String result = StringUtils.isEmpty(inShowUsername) ? username : inShowUsername;
            jsCallback.execute(result);
        }
    }

    public static boolean pluginValidate(PluginView pluginView) {
        return StringUtils.isNotEmpty(pluginView.getID())
                && StringUtils.isNotEmpty(pluginView.getName())
                && StringUtils.isNotEmpty(pluginView.getVersion())
                && StringUtils.isNotEmpty(pluginView.getEnvVersion());
    }

    public static String getSuccessInfo(PluginTaskResult result){
        StringBuilder pluginInfo = new StringBuilder();
        PluginTask currentTask = result.getCurrentTask();
        PluginContext context = PluginManager.getContext(currentTask.getMarker());
        if(context != null){
            pluginInfo.append(context.getName());
        }
        List<PluginTaskResult> pluginTaskResults = result.asList();
        for(PluginTaskResult pluginTaskResult : pluginTaskResults){
            List<PluginTask> pluginTasks = pluginTaskResult.getPreTasks();
            for(PluginTask pluginTask : pluginTasks){
                PluginContext pluginContext = PluginManager.getContext(pluginTask.getMarker());
                if(pluginContext != null){
                    pluginInfo.append(pluginContext.getName());
                }
            }
        }
        return pluginInfo.toString();
    }

}
