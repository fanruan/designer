package com.fr.design.extra;

import com.fr.design.extra.exe.*;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 开放给Web组件的接口,用于安装,卸载,更新以及更改插件可用状态
 */
public class PluginWebBridge {

    private static PluginWebBridge helper;

    public static PluginWebBridge getHelper(WebEngine webEngine) {
        if (helper != null) {
            return helper;
        }
        synchronized (PluginWebBridge.class) {
            if (helper == null) {
                helper = new PluginWebBridge(webEngine);
            }
            return helper;
        }
    }

    private WebEngine webEngine;

    private PluginWebBridge(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    /**
     * 从插件服务器上安装插件
     *
     * @param pluginID 插件的ID
     * @param callback 回调函数
     */
    public void installPluginOnline(final String pluginID, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new InstallOnlineExecutor(pluginID));
        new Thread(task).start();
    }

    /**
     * 从磁盘上选择插件安装包进行安装
     *
     * @param filePath 插件包的路径
     */
    public void installPluginFromDisk(final String filePath, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new InstallFromDiskExecutor(filePath));
        new Thread(task).start();
    }

    /**
     * 卸载当前选中的插件
     *
     * @param pluginIDs 插件集合
     */
    public void uninstallPlugin(JSObject pluginIDs, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new UninstallExecutor(jsObjectToStringArray(pluginIDs)));
        new Thread(task).start();
    }

    /**
     * 从插件服务器上更新选中的插件
     *
     * @param pluginID 插件的ID
     */
    public void updatePluginOnline(String pluginID, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new UpdateOnlineExecutor(pluginID));
        new Thread(task).start();
    }

    /**
     * 从磁盘上选择插件安装包进行插件升级
     *
     * @param filePath 插件包的路径
     */
    public void updatePluginFromDisk(String filePath, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new UpdateFromDiskExecutor(filePath));
        new Thread(task).start();
    }

    /**
     * 修改选中的插件的活跃状态
     *
     * @param pluginID 插件ID
     * @param active   如果要把插件修改为激活状态,则为true,否则为false
     */
    public void setPluginActive(String pluginID, boolean active, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new ModifyStatusExecutor(pluginID, active));
        new Thread(task).start();
    }

    /**
     * 选择文件对话框
     *
     * @return 选择的文件的路径
     */
    public String showFileChooser() {
        return showFileChooserWithFilter(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 选择文件对话框
     *
     * @param des    过滤文件描述
     * @param filter 文件的后缀
     * @return 选择的文件的路径
     */
    public String showFileChooserWithFilter(String des, String filter) {
        FileChooser fileChooser = new FileChooser();

        if (StringUtils.isNotEmpty(filter)) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(des, filter));
        }

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            return null;
        }
        return selectedFile.getAbsolutePath();
    }

    /**
     * 选择文件对话框
     *
     * @param des  过滤文件描述
     * @param args 文件的后缀
     * @return 选择的文件的路径
     */
    public String showFileChooserWithFilters(String des, JSObject args) {
        FileChooser fileChooser = new FileChooser();
        String[] filters = jsObjectToStringArray(args);
        if (ArrayUtils.isNotEmpty(filters)) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(des, filters));
        }

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            return null;
        }
        return selectedFile.getAbsolutePath();
    }

    /**
     * 获取已经安装的插件的数组
     * @return 已安装的插件组成的数组
     */
    public Plugin[] getInstalledPlugins() {
        return PluginLoader.getLoader().getInstalled();
    }

    private String[] jsObjectToStringArray(JSObject obj) {
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
