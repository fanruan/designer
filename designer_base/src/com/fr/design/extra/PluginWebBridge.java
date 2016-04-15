package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.exe.*;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 开放给Web组件的接口,用于安装,卸载,更新以及更改插件可用状态
 */
public class PluginWebBridge {

    private static PluginWebBridge helper;

    private UIDialog uiDialog;

    public static PluginWebBridge getHelper() {
        if (helper != null) {
            return helper;
        }
        synchronized (PluginWebBridge.class) {
            if (helper == null) {
                helper = new PluginWebBridge();
            }
            return helper;
        }
    }

    public static PluginWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        helper.setEngine(webEngine);
        return helper;
    }

    private WebEngine webEngine;

    private PluginWebBridge() {
    }

    public void setEngine(WebEngine webEngine){
        this.webEngine = webEngine;
    }

    public void setDialogHandle(UIDialog uiDialog){
        this.uiDialog = uiDialog;
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
     * @param pluginIDs 插件集合
     */
    public void updatePluginOnline(JSObject pluginIDs, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new UpdateOnlineExecutor(jsObjectToStringArray(pluginIDs)));
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
     */
    public void setPluginActive(String pluginID, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new ModifyStatusExecutor(pluginID));
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
     *
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

    public String getPluginFromStore(String category, String seller, String fee) {

        StringBuilder url = new StringBuilder("http://127.0.0.1:8080/ShopServer?pg=plist");
        if (StringUtils.isNotBlank(category)) {
            url.append("&cid=").append(category.split("-")[1]);
        }
        if (StringUtils.isNotBlank(seller)) {
            url.append("&seller=").append(seller.split("-")[1]);
        }
        if (StringUtils.isNotBlank(fee)) {
            url.append("&fee=").append(fee.split("-")[1]);
        }
        String resText = null;
        try {
            HttpClient httpClient = new HttpClient(url.toString());
            httpClient.setTimeout(3000);
            resText = httpClient.getResponseText();
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return resText == null ? StringUtils.EMPTY : resText;
    }


    public void showRestartMessage(String message){
        int rv = JOptionPane.showOptionDialog(
                null,
                message,
                Inter.getLocText("FR-Designer-Plugin_Warning"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"), Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")},
                null
        );
        if (rv == JOptionPane.OK_OPTION) {
            RestartHelper.restart();
        }
    }

    public void closeWindow() {
        if (uiDialog != null) {
            uiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            uiDialog.setVisible(false);
        }
    }

    public void updateFileFromDisk(File fileOnDisk){
        try {
            Plugin plugin = PluginHelper.readPlugin(fileOnDisk);
            if (plugin == null) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Illegal_Plugin_Zip"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Plugin oldPlugin = PluginLoader.getLoader().getPluginById(plugin.getId());
            if (oldPlugin != null) {
                // 说明安装了同ID的插件，再比较两个插件的版本
                if (PluginHelper.isNewThan(plugin, oldPlugin)) {
                    // 说明是新的插件，删除老的然后安装新的
                    final String[] files = PluginHelper.uninstallPlugin(FRContext.getCurrentEnv(), oldPlugin);
                    PluginHelper.installPluginFromUnzippedTempDir(FRContext.getCurrentEnv(), plugin, new After() {
                        @Override
                        public void done() {
                            int rv = JOptionPane.showOptionDialog(
                                    null,
                                    Inter.getLocText("FR-Designer-Plugin_Update_Successful"),
                                    Inter.getLocText("FR-Designer-Plugin_Warning"),
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"),
                                            Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")
                                    },
                                    null
                            );

                            if (rv == JOptionPane.OK_OPTION) {
                                RestartHelper.restart();
                            }

                            // 如果不是立即重启，就把要删除的文件存放起来
                            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                                RestartHelper.saveFilesWhichToDelete(files);
                            }
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Version_Is_Lower_Than_Current"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Cannot_Update_Not_Install"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
