package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.plugin.PluginVerifyException;
import com.fr.stable.StableUtils;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by vito on 2016/9/28.
 */
public class WebViewDlgHelper {
    private static final String LATEST = "latest";
    private static final String SHOP_SCRIPTS = "shop_scripts";
    private static final int VERSION_8 = 8;
    // 调试时，使用installHome = ClassLoader.getSystemResource("").getPath()代替下面
    private static String installHome = StableUtils.getInstallHome();

    public static void createPluginDialog() {
        if (StableUtils.getMajorJavaVersion() >= VERSION_8) {
            String relativePath = "/scripts/store/web/index.html";
            String mainJsPath = StableUtils.pathJoin(new File(installHome).getAbsolutePath(), relativePath);
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
                updateShopScripts(SHOP_SCRIPTS);
                showPluginDlg(mainJsPath);
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
     * 以关键词打开设计器商店
     *
     * @param keyword 关键词
     */
    public static void createPluginDialog(String keyword) {
        PluginWebBridge.getHelper().openWithSearch(keyword);
        createPluginDialog();
    }

    public static void createLoginDialog() {
        if (StableUtils.getMajorJavaVersion() == VERSION_8) {
            File file = new File(StableUtils.pathJoin(installHome, "scripts"));
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
                showLoginDlg();
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

    private static void showPluginDlg(String mainJsPath) {
        try {
            Class<?> clazz = Class.forName("com.fr.design.extra.PluginWebPane");
            Constructor constructor = clazz.getConstructor(String.class);
            Component webPane = (Component) constructor.newInstance(mainJsPath);

            BasicPane managerPane = new ShopManagerPane(webPane);
            UIDialog dlg = new ShopDialog(DesignerContext.getDesignerFrame(), managerPane);
            PluginWebBridge.getHelper().setDialogHandle(dlg);
            dlg.setVisible(true);
        } catch (Throwable ignored) {
            // ignored
        }
    }

    private static void showLoginDlg() {
        try {
            Class<?> clazz = Class.forName("com.fr.design.extra.LoginWebPane");
            Constructor constructor = clazz.getConstructor(String.class);
            Component webPane = (Component) constructor.newInstance(new File(installHome).getAbsolutePath());

            UIDialog qqdlg = new LoginDialog(DesignerContext.getDesignerFrame(), webPane);
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
                String username = DesignerEnvManager.getEnvManager().getBBSName();
                String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                try {
                    PluginHelper.downloadPluginFile(scriptsId, username, password, new Process<Double>() {
                        @Override
                        public void process(Double integer) {
                            // ignored
                        }
                    });
                } catch (PluginVerifyException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                    return false;
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                    return false;
                }
                return true;
            }

            @Override
            protected void done() {

                try {
                    if (get()) {
                        IOUtils.unzip(new File(StableUtils.pathJoin(PluginHelper.DOWNLOAD_PATH, PluginHelper.TEMP_FILE)), StableUtils.getInstallHome());
                        int rv = JOptionPane.showOptionDialog(
                                null,
                                Inter.getLocText("FR-Designer-Plugin_Shop_Installed"),
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
                } catch (InterruptedException | ExecutionException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }

            }
        }.execute();
    }

    private static void updateShopScripts(final String scriptsId) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("store.version") + "&version=" + PluginStoreConstants.VERSION);
                if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String text = httpClient.getResponseText();
                    if (!ComparatorUtils.equals(text, LATEST)) {
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
