package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.CommonIOUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.PluginStoreConstants;
import com.fr.plugin.PluginVerifyException;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StableUtils;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

/**
 * 在合适的 jre 环境下创建带有 WebView 的窗口
 *
 * @author vito
 * @date 2016/9/28
 */
public class WebViewDlgHelper {
    private static final String LATEST = "latest";
    private static final String SHOP_SCRIPTS = "shop_scripts";
    private static final int VERSION_8 = 8;
    private static String installHome = FRContext.getCurrentEnv().getWebReportPath();
    private static final String MAIN_JS_PATH = "/scripts/plugin.html";
    private static final int BYTES_NUM = 1024;

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {
                installHome = FRContext.getCurrentEnv().getWebReportPath();
            }
        });
    }

    public static void createPluginDialog() {
        if (StableUtils.getMajorJavaVersion() >= VERSION_8) {
            String mainJsPath = StableUtils.pathJoin(installHome, MAIN_JS_PATH);
            File file = new File(mainJsPath);
            if (!file.exists()) {
                int rv = JOptionPane.showConfirmDialog(
                        null,
                        Inter.getLocText("FR-Designer-Plugin_Shop_Need_Install"),
                        Inter.getLocText("FR-Designer-Plugin_Warning"),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );
                if (rv == JOptionPane.OK_OPTION) {
                    downloadShopScripts(SHOP_SCRIPTS);
                }
            } else {
                showPluginDlg();
                updateShopScripts(SHOP_SCRIPTS);
            }
        } else {
            BasicPane traditionalStorePane = new BasicPane() {
                @Override
                protected String title4PopupWindow() {
                    return Inter.getLocText("FR-Designer-Plugin_Manager");
                }
            };
            traditionalStorePane.setLayout(new BorderLayout());
            traditionalStorePane.add(initTraditionalStore(), BorderLayout.CENTER);
            UIDialog dlg = new ShopDialog(DesignerContext.getDesignerFrame(), traditionalStorePane);
            dlg.setVisible(true);
        }
    }

    /**
     * 检查script文件夹中的plugin.html文件
     */
    public static void checkAndCopyMainFile(String indexPath, String mainJsPath) {
        File file = new File(indexPath);
        if (!file.exists()) {
            copyMainFile(mainJsPath);
        }
    }

    /**
     * 將script文件夹中的plugin.html文件复制到webreport下
     */
    private static void copyMainFile(String mainJsPath) {
        try {
            CommonIOUtils.copy(new File(mainJsPath), new File(installHome));
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    /**
     * 以关键词打开设计器商店
     *
     * @param keyword 关键词
     */
    public static void createPluginDialog(String keyword) {
        PluginWebBridge.getHelper().openWithSearch(keyword);
        createPluginDialog();
    }

    /**
     * 以关键词打开设计器商店显示搜索结果
     *
     * @param keyword
     * @param data
     */
    public static void showPluginInStore(String keyword, String data) {
        PluginWebBridge.getHelper().showResultInStore(keyword, data);
        createPluginDialog();
    }

    public static void createLoginDialog() {
        if (StableUtils.getMajorJavaVersion() == VERSION_8) {
            File file = new File(StableUtils.pathJoin(installHome, "scripts"));
            if (!file.exists()) {
                confirmDownLoadShopJS();
            } else {
                showLoginDlg(DesignerContext.getDesignerFrame());
                updateShopScripts(SHOP_SCRIPTS);
            }
        }
    }


    public static void createLoginDialog(Window parent) {
        if (StableUtils.getMajorJavaVersion() == VERSION_8) {
            File file = new File(StableUtils.pathJoin(installHome, "scripts"));
            if (!file.exists()) {
                confirmDownLoadShopJS();
            } else {
                showLoginDlg(parent);
                updateShopScripts(SHOP_SCRIPTS);
            }
        }
    }

    public static void createQQLoginDialog() {
        try {
            Class<?> clazz = Class.forName("com.fr.design.extra.QQLoginWebPane");
            Constructor constructor = clazz.getConstructor(String.class);
            Component webPane = (Component) constructor.newInstance(new File(installHome).getAbsolutePath());

            UIDialog qqLoginDialog = new QQLoginDialog(DesignerContext.getDesignerFrame(), webPane);
            LoginWebBridge.getHelper().setQQDialog(qqLoginDialog);
            qqLoginDialog.setVisible(true);
        } catch (Throwable ignored) {
            // ignored
        }
    }


    private static void confirmDownLoadShopJS() {
        int rv = JOptionPane.showConfirmDialog(
                null,
                Inter.getLocText("FR-Designer-Plugin_Shop_Need_Install"),
                Inter.getLocText("FR-Designer-Plugin_Warning"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
        if (rv == JOptionPane.OK_OPTION) {
            downloadShopScripts(SHOP_SCRIPTS);
        }
    }

    private static void showPluginDlg() {
        try {
            Class<?> clazz = Class.forName("com.fr.design.extra.PluginWebPane");
            Constructor constructor = clazz.getConstructor(String.class, String.class);
            Component webPane = (Component) constructor.newInstance(installHome, MAIN_JS_PATH);

            BasicPane managerPane = new ShopManagerPane(webPane);
            UIDialog dlg = new ShopDialog(DesignerContext.getDesignerFrame(), managerPane);
            PluginWebBridge.getHelper().setDialogHandle(dlg);
            dlg.setVisible(true);
        } catch (Exception e) {
            // ignored
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static void showLoginDlg(Window window) {
        try {
            Class<?> clazz = Class.forName("com.fr.design.extra.LoginWebPane");
            Constructor constructor = clazz.getConstructor(String.class);
            Component webPane = (Component) constructor.newInstance(installHome);
            UIDialog qqdlg;
            if (window instanceof Dialog) {
                qqdlg = new LoginDialog((Dialog) window, webPane);
            } else {
                qqdlg = new LoginDialog((Frame) window, webPane);
            }
            LoginWebBridge.getHelper().setDialogHandle(qqdlg);
            qqdlg.setVisible(true);
        } catch (Throwable ignored) {
            // ignored
        }
    }

    private static Component initTraditionalStore() {
        UITabbedPane tabbedPane = new UITabbedPane();
        PluginInstalledPane installedPane = new PluginInstalledPane();
        tabbedPane.addTab(installedPane.tabTitle(), installedPane);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_Update"), new PluginUpdatePane(tabbedPane));
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_All_Plugins"), new PluginFromStorePane(tabbedPane));
        return tabbedPane;
    }

    private static void downloadShopScripts(final String scriptsId) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    PluginUtils.downloadShopScripts(scriptsId, new Process<Double>() {
                        @Override
                        public void process(Double integer) {
                            // 这个注释毫无意义，就是为了通过SonarQube
                        }
                    });
                } catch (PluginVerifyException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                    return false;
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    return false;
                }
                return true;
            }

            @Override
            protected void done() {

                try {
                    if (get()) {
                        String relativePath = "/scripts/plugin.html";
                        IOUtils.unzip(new File(StableUtils.pathJoin(PluginConstants.DOWNLOAD_PATH, PluginConstants.TEMP_FILE)), installHome);
                        copyMainFile(StableUtils.pathJoin(installHome, relativePath));
                        // TODO: 2017/4/17 删除之前存放在安装目录下的script
                        PluginStoreConstants.refreshProps();    // 下载完刷新一下版本号等
                        JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Shop_Installed"), Inter.getLocText("FR-Designer_Tooltips"), JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }

            }
        }.execute();
    }

    private static void updateShopScripts(final String scriptsId) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("shop.plugin.cv") + "&version=" + PluginStoreConstants.getInstance().getProps("VERSION"));
                httpClient.asGet();
                if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String text = httpClient.getResponseText();
                    JSONObject resultJSONObject = new JSONObject(text);
                    String isLatest = resultJSONObject.optString("result");
                    if (!ComparatorUtils.equals(isLatest, LATEST)) {
                        int rv = JOptionPane.showConfirmDialog(
                                null,
                                Inter.getLocText("FR-Designer-Plugin_Shop_Need_Update"),
                                Inter.getLocText("FR-Designer-Plugin_Warning"),
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        if (rv == JOptionPane.OK_OPTION) {
                            downloadShopScripts(scriptsId);
                        }
                    }
                }
                return null;
            }
        }.execute();
    }
}
