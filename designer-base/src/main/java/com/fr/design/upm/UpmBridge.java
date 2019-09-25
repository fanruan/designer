package com.fr.design.upm;

import com.fr.base.passport.FinePassportManager;
import com.fr.config.MarketConfig;
import com.fr.config.ServerPreferenceConfig;
import com.fr.decision.webservice.v10.plugin.helper.category.impl.UpmResourceLoader;
import com.fr.design.bridge.exec.JSBridge;
import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.extra.PluginOperateUtils;
import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.exe.GetInstalledPluginsExecutor;
import com.fr.design.extra.exe.GetPluginCategoriesExecutor;
import com.fr.design.extra.exe.GetPluginFromStoreExecutor;
import com.fr.design.extra.exe.GetPluginPrefixExecutor;
import com.fr.design.extra.exe.PluginLoginExecutor;
import com.fr.design.extra.exe.ReadUpdateOnlineExecutor;
import com.fr.design.extra.exe.SearchOnlineExecutor;
import com.fr.design.i18n.Toolkit;
import com.fr.design.upm.event.CertificateEvent;
import com.fr.design.upm.event.DownloadEvent;
import com.fr.design.upm.exec.UpmBrowserExecutor;
import com.fr.design.upm.task.UpmTaskWorker;
import com.fr.event.EventDispatcher;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralUtils;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginMarker;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSArray;
import com.teamdev.jxbrowser.chromium.JSFunction;
import com.teamdev.jxbrowser.chromium.JSObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 * 桥接Java和JavaScript的类
 */
public class UpmBridge {

    public static UpmBridge getBridge(Browser browser) {
        return new UpmBridge(browser);
    }

    private JSObject window;

    private UpmBridge(Browser browser) {
        this.window = browser.executeJavaScriptAndReturnValue("window").asObject();
    }

    /**
     * 更新插件管理中心资源文件，这个方法仅仅是为了语义上的作用（更新）
     * @param callback 安装完成后的回调函数
     */
    @JSBridge
    public void update(final JSFunction callback) {
        callback.invoke(window, "start", Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download_Start"));
        try {
            UpmResourceLoader.INSTANCE.download();
            UpmResourceLoader.INSTANCE.install();
            callback.invoke(window, "success", Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download_Success"));
            EventDispatcher.fire(DownloadEvent.UPDATE, "success");
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            callback.invoke(window, "error", Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download_Error"));
        }
    }

    /**
     * 下载并安装插件管理中心的资源文件
     * @param callback 安装完成后的回调函数
     */
    @JSBridge
    public void startDownload(final JSFunction callback) {
        callback.invoke(window, "start", Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download_Start"));
        new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                UpmResourceLoader.INSTANCE.download();
                UpmResourceLoader.INSTANCE.install();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    callback.invoke(window, "success", Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download_Success"));
                    EventDispatcher.fire(DownloadEvent.SUCCESS, "success");
                } catch (Exception e) {
                    callback.invoke(window, "error", Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download_Error"));
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    EventDispatcher.fire(DownloadEvent.ERROR, "error");
                }
            }
        }.execute();
    }

    /**
     * 获取upm的版本信息
     * @return 版本信息
     */
    @JSBridge
    public String getVersion() {
        return ServerPreferenceConfig.getInstance().getOptimizedUPMVersion();
    }

    @JSBridge
    public String i18nText(String key) {
        return Toolkit.i18nText(key);
    }

    @JSBridge
    public void closeWindow() {
        UpmFinder.closeWindow();
    }


    @JSBridge
    public boolean isDesigner() {
        return true;
    }

    @JSBridge
    public void getPackInfo(final JSFunction callback) {
        callback.invoke(window, StringUtils.EMPTY);
    }

    @JSBridge
    public void getPluginPrefix(final JSFunction callback) {
        UpmTaskWorker<Void> task = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new GetPluginPrefixExecutor());
        task.execute();
    }

    /**
     * 在线获取插件分类
     *
     * @param callback 回调函数
     */
    @JSBridge
    public void getPluginCategories(final JSFunction callback) {
        UpmTaskWorker<Void> task = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new GetPluginCategoriesExecutor());
        task.execute();
    }

    /**
     * 根据条件获取在线插件
     *
     * @param info     插件信息
     * @param callback 回调函数
     */
    @JSBridge
    public void getPluginFromStoreNew(String info, final JSFunction callback) {
        UpmTaskWorker<Void> task = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new GetPluginFromStoreExecutor(new JSONObject(info)));
        task.execute();
    }

    /**
     * 已安装插件检查更新
     */
    @JSBridge
    public void readUpdateOnline(final JSFunction callback) {
        UpmTaskWorker<Void> task = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new ReadUpdateOnlineExecutor());
        task.execute();
    }

    /**
     * 获取已经安装的插件的数组
     */
    @JSBridge
    public void getInstalledPlugins(final JSFunction callback) {
        UpmTaskWorker<Void> task = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new GetInstalledPluginsExecutor());
        task.execute();
    }

    /**
     * 从插件服务器上更新选中的插件
     *
     * @param pluginIDs 插件集合
     */
    @JSBridge
    public void updatePluginOnline(Object pluginIDs, final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        List<PluginMarker> pluginMarkerList = new ArrayList<>();
        if (pluginIDs instanceof String) {
            pluginMarkerList.add(PluginUtils.createPluginMarker(pluginIDs.toString()));
        } else if (pluginIDs instanceof JSArray) {
            JSArray pluginInfos = (JSArray) pluginIDs;
            for (int i = 0, len = pluginInfos.length(); i < len; i++) {
                String value = pluginInfos.get(i).asString().getValue();
                pluginMarkerList.add(PluginUtils.createPluginMarker(value));
            }
        }
        PluginOperateUtils.updatePluginOnline(pluginMarkerList, jsCallback);
    }

    /**
     * 搜索在线插件
     *
     * @param keyword 关键字
     */
    @JSBridge
    public void searchPlugin(String keyword, final JSFunction callback) {
        UpmTaskWorker<Void> worker = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new SearchOnlineExecutor(keyword));
        worker.execute();
    }

    /**
     * 从磁盘上选择插件安装包进行安装
     *
     * @param filePath 插件包的路径
     */
    @JSBridge
    public void installPluginFromDisk(final String filePath, final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        File file = new File(filePath);
        PluginOperateUtils.installPluginFromDisk(file, jsCallback);
    }

    /**
     * 卸载当前选中的插件
     *
     * @param pluginInfo 插件信息
     */
    @JSBridge
    public void uninstallPlugin(final String pluginInfo, final boolean isForce, final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        PluginOperateUtils.uninstallPlugin(pluginInfo, isForce, jsCallback);
    }

    /**
     * 从插件服务器上安装插件
     *
     * @param pluginInfo 插件的ID
     * @param callback   回调函数
     */
    @JSBridge
    public void installPluginOnline(final String pluginInfo, final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
        PluginOperateUtils.installPluginOnline(pluginMarker, jsCallback);
    }

    /**
     * 从磁盘上选择插件安装包进行插件升级
     *
     * @param filePath 插件包的路径
     */
    public void updatePluginFromDisk(String filePath, final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        File file = new File(filePath);
        PluginOperateUtils.updatePluginFromDisk(file, jsCallback);
    }

    /**
     * 修改选中的插件的活跃状态
     *
     * @param pluginID 插件ID
     */
    @JSBridge
    public void setPluginActive(String pluginID, final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        PluginOperateUtils.setPluginActive(pluginID, jsCallback);
    }

    /**
     * 选择文件对话框
     *
     * @return 选择的文件的路径
     */
    @JSBridge
    public String showFileChooser() {
        return showFileChooserWithFilter(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 选择文件对话框
     *
     * @param des    过滤文件描述
     * @param filter 文件的后缀
     * @return 选择的文件的路径
     * 这里换用JFileChooser会卡死,不知道为什么
     */
    @JSBridge
    public String showFileChooserWithFilter(final String des, final String filter) {
        RunnableFuture<String> future = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (StringUtils.isNotEmpty(filter)) {
                    fileChooser.setFileFilter(new FileNameExtensionFilter(des, UpmUtils.findMatchedExtension(filter)));
                }

                int result = fileChooser.showOpenDialog(UpmFinder.getDialog());
                if (result == JFileChooser.APPROVE_OPTION) {
                    return fileChooser.getSelectedFile().getAbsolutePath();
                }
                return null;
            }
        });
        SwingUtilities.invokeLater(future);
        try {
            return future.get();
        } catch (Exception e) {
           FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 选择文件对话框
     *
     * @param des  过滤文件描述
     * @param args 文件的后缀
     * @return 选择的文件的路径
     */
    @JSBridge
    public String showFileChooserWithFilters(final String des, final Object args) {
        RunnableFuture<String> future = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() {
                JFileChooser fileChooser = new JFileChooser();
                List<String> filterList = new ArrayList<>();
                if (args instanceof  String) {
                    filterList.add(GeneralUtils.objectToString(args));
                } else if (args instanceof JSArray) {
                    JSArray array = (JSArray)args;
                    for (int i = 0, len = array.length(); i < len; i ++) {
                        filterList.add(array.get(i).getStringValue());
                    }
                }
                String[] filters = filterList.toArray(new String[0]);
                if (ArrayUtils.isNotEmpty(filters)) {
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(des, UpmUtils.findMatchedExtension(filters));
                    fileChooser.setFileFilter(filter);
                }
                int result = fileChooser.showOpenDialog(UpmFinder.getDialog());
                if (result == JFileChooser.APPROVE_OPTION) {
                    return fileChooser.getSelectedFile().getAbsolutePath();
                }
                return null;
            }
        });
        SwingUtilities.invokeLater(future);
        try {
            return future.get();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    ////////登录相关///////

    /**
     * 获取系统登录的用户名
     */
    @JSBridge
    public String getLoginInfo(final JSFunction callback) {
        registerLoginInfo(callback);
        return MarketConfig.getInstance().getBbsUsername();
    }

    /**
     * 系统登录注册
     *
     * @param callback 回调函数
     */
    @JSBridge
    public void registerLoginInfo(final JSFunction callback) {
        JSCallback jsCallback = new JSCallback(UpmBrowserExecutor.create(window, callback));
        String username = MarketConfig.getInstance().getBbsUsername();
        if (StringUtils.isEmpty(username)) {
            jsCallback.execute(StringUtils.EMPTY);
            EventDispatcher.fire(CertificateEvent.LOGOUT, StringUtils.EMPTY);
        } else {
            jsCallback.execute(username);
            EventDispatcher.fire(CertificateEvent.LOGIN, username);
        }
    }


    /**
     * 设计器端的用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param callback 回调函数
     */
    @JSBridge
    public void defaultLogin(String username, String password, final JSFunction callback) {
        UpmTaskWorker<Void> worker = new UpmTaskWorker<>(new JSCallback(UpmBrowserExecutor.create(window, callback)), new PluginLoginExecutor(username, password));
        worker.execute();
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
        MarketConfig.getInstance().setInShowBBsName(StringUtils.EMPTY);
        FinePassportManager.getInstance().logout();
        EventDispatcher.fire(CertificateEvent.LOGOUT, StringUtils.EMPTY);
    }

    /**
     * 打开论坛消息界面
     */
    @JSBridge
    public void getPriviteMessage() {
        try {
            String loginUrl = CloudCenter.getInstance().acquireUrlByKind("bbs.default");
            Desktop.getDesktop().browse(new URI(loginUrl));
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().info(exp.getMessage());
        }
    }

    /**
     * 忘记密码
     */
    @JSBridge
    public void forgetHref() {
        try {
            Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.reset")));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info(e.getMessage());
        }
    }

    /**
     * 立即注册
     */
    @JSBridge
    public void registerHref() {
        try {
            Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.register")));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info(e.getMessage());
        }
    }

    /**
     * 使用系统浏览器打开网页
     * @param url 要打开的网页
     */
    @JSBridge
    public void openShopUrlAtWebBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                //创建一个URI实例,注意不是URL
                URI uri = URI.create(url);
                //获取当前系统桌面扩展
                Desktop desktop = Desktop.getDesktop();
                //判断系统桌面是否支持要执行的功能
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    //获取系统默认浏览器打开链接
                    desktop.browse(uri);
                }
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
}
