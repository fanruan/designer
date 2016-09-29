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
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 * 应用中心的构建采用JavaScript代码来动态实现,但是不总是依赖于服务器端的HTML
 * 采用JDK提供的JavaScript引擎,实际是用JavaScript语法实现Java端的功能,并通过JavaScript引擎动态调用
 * JavaScript放在安装目录下的scripts/store目录下,检测到新版本的时候,可以通过更新这个目录下的文件实现热更新
 * 不直接嵌入WebView组件的原因是什么呢?
 * 因为如果直接嵌入WebView,和设计器的交互就需要预先设定好,这样灵活性会差很多,而如果使用JavaScript引擎,
 * 就可以直接在JavaScript中和WebView组件做交互,而同时JavaScript中可以调用任何的设计器API.
 */
public class ShopManagerPane extends BasicPane {

    private static final String LATEST = "latest";
    private ShopPaneConfig shopPaneConfig;

    public ShopManagerPane(ShopPaneConfig shopPaneConfig) {
        this.shopPaneConfig = shopPaneConfig;
        setLayout(new BorderLayout());
        if (StableUtils.isDebug()) {
            addPane();
        } else {
            File file = new File(shopPaneConfig.getMainJS());
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
                addPane();
                updateShopScripts();
            }
        }
    }

    private void addPane() {
        JFXPanel webPane = shopPaneConfig.getWebPane();
        add(webPane, BorderLayout.CENTER);
    }

    private void downloadShopScripts() {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                String username = DesignerEnvManager.getEnvManager().getBBSName();
                String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                try {
                    PluginHelper.downloadPluginFile(shopPaneConfig.getScriptsId(), username, password, new Process<Double>() {
                        @Override
                        public void process(Double integer) {
                        }
                    });
                } catch (PluginVerifyException e) {
                    JOptionPane.showMessageDialog(ShopManagerPane.this, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
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
                                ShopManagerPane.this,
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
                                ShopManagerPane.this,
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

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}