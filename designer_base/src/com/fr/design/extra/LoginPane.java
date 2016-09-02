package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.plugin.PluginVerifyException;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by zhaohehe on 16/7/27.
 */
public class LoginPane extends BasicPane {
    private static final String LATEST = "latest";

    public LoginPane() {
        setLayout(new BorderLayout());
        if (StableUtils.getMajorJavaVersion() == 8) {
            String installHome;
            if (StableUtils.isDebug()) {
                URL url = ClassLoader.getSystemResource("");
                installHome = url.getPath();
                addPane(installHome);
            } else {
                installHome = StableUtils.getInstallHome();
                File file = new File(StableUtils.pathJoin(installHome, "scripts"));
                if (!file.exists()) {
                    int rv = JOptionPane.showConfirmDialog(
                            this,
                            Inter.getLocText("FR-Designer-Plugin_Shop_Need_Install"),
                            Inter.getLocText("FR-Designer-Plugin_Warning"),
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    if (rv == JOptionPane.OK_OPTION) {
                        downloadShopScripts();
                    }
                } else {
                    addPane(installHome);
                    updateShopScripts();
                }
            }
        } else {
        }
    }

    private void addPane(String installHome) {
        LoginWebPane webPane = new LoginWebPane(new File(installHome).getAbsolutePath(),LoginPane.this);
        add(webPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }

    private void downloadShopScripts() {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                String id = "shop_scripts";
                String username = DesignerEnvManager.getEnvManager().getBBSName();
                String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                try {
                    PluginHelper.downloadPluginFile(id, username, password, new Process<Double>() {
                        @Override
                        public void process(Double integer) {
                        }
                    });
                } catch (PluginVerifyException e) {
                    JOptionPane.showMessageDialog(LoginPane.this, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
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
                                LoginPane.this,
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

    private void updateShopScripts() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("store.version") + "&version=" + PluginStoreConstants.VERSION);
                if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    if (!ComparatorUtils.equals(httpClient.getResponseText(), LATEST)) {
                        int rv = JOptionPane.showConfirmDialog(
                                LoginPane.this,
                                Inter.getLocText("FR-Designer-Plugin_Shop_Need_Update"),
                                Inter.getLocText("FR-Designer-Plugin_Warning"),
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        if (rv == JOptionPane.OK_OPTION) {
                            downloadShopScripts();
                        }
                    }
                }
                return null;
            }
        }.execute();
    }
}