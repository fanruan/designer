package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.extra.exe.callback.*;
import com.fr.design.extra.exe.extratask.InstallPluginTask;
import com.fr.design.extra.exe.extratask.UpdatePluginTask;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.bbs.BBSPluginLogin;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import sun.plugin2.main.server.Plugin;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ibm on 2017/5/26.
 */
public class PluginOperateUtils {

    public static void installPluginOnline(final String pluginInfo, JSCallback jsCallback) {
        //下载插件
        if (!BBSPluginLogin.getInstance().hasLogin()) {
            LoginCheckContext.fireLoginCheckListener();
        }
        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
        if (BBSPluginLogin.getInstance().hasLogin()) {
            PluginManager.getController().download(pluginMarker, new DownloadCallback(new InstallPluginTask(pluginMarker, jsCallback), jsCallback));
        }
    }

    public static void installPluginFromDisk(final String filePath, JSCallback jsCallback) {
        PluginManager.getController().install(new File(filePath), new InstallFromDiskCallback(new File(filePath), jsCallback));
    }

    public static void updatePluginOnline(JSObject pluginIDs, JSCallback jsCallback) {
        String[] pluginInfos = jsObjectToStringArray(pluginIDs);
        if (!(BBSPluginLogin.getInstance().hasLogin())) {
            LoginCheckContext.fireLoginCheckListener();
        }
        if (BBSPluginLogin.getInstance().hasLogin()) {
            List<PluginMarker> pluginMarkerList = new ArrayList<PluginMarker>();
            for (int i = 0; i < pluginInfos.length; i++) {
                pluginMarkerList.add(PluginUtils.createPluginMarker(pluginInfos[i]));
            }
            for (int i = 0; i < pluginMarkerList.size(); i++) {
                try {
                    //todo check下此插件的最新版本
                    String latestPluginInfo = PluginUtils.getLatestPluginInfo(pluginMarkerList.get(i).getPluginID());
                    if (StringUtils.isEmpty(latestPluginInfo) || PluginConstants.CONNECTION_404.equals(latestPluginInfo)) {
                        JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Connect_Failed"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JSONObject resultArr = new JSONObject(latestPluginInfo);
                    String latestPluginVersion = (String) resultArr.get("version");
                    PluginMarker pluginMarker = pluginMarkerList.get(i);
                    PluginMarker toPluginMarker = PluginMarker.create(pluginMarkerList.get(i).getPluginID(), latestPluginVersion);
                    PluginManager.getController().download(pluginMarkerList.get(i), new DownloadCallback(new UpdatePluginTask(pluginMarker, toPluginMarker, jsCallback), jsCallback));
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }
    }

    public static void updatePluginFromDisk(final String filePath, JSCallback jsCallback) {
        PluginManager.getController().update(new File(filePath), new UpdateFromDiskCallback(new File(filePath), jsCallback));
    }

    public static void setPluginActive(String pluginInfo, JSCallback jsCallback) {
        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
        PluginContext plugin = PluginManager.getContext(pluginMarker);
        boolean active = !plugin.isActive();
        PluginTaskCallback modifyStatusCallback = new ModifyStatusCallback(active);
        if (active) {
            PluginManager.getController().forbid(pluginMarker, modifyStatusCallback);
        } else {
            PluginManager.getController().enable(pluginMarker, modifyStatusCallback);
        }
    }

    public static void uninstallPlugin(final String pluginInfo, final boolean isForce, JSCallback jsCallback) {
        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
        PluginManager.getController().uninstall(pluginMarker, isForce, new UnistallPluginCallback());
    }

    private static String[] jsObjectToStringArray(JSObject obj) {
        if (obj == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int len = (int) obj.getMember("length");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(obj.getSlot(i).toString());
        }
        return list.toArray(new String[len]);
    }

}
