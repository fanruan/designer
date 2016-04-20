package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

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
public class PluginManagerPane extends BasicPane {


    public PluginManagerPane() {
        setLayout(new BorderLayout());
        if (System.getProperty("java.version").startsWith("1.8")) {
            String installHome;
            if(StableUtils.isDebug()){
                URL url = ClassLoader.getSystemResource("");
                 installHome = url.getPath() + "scripts";
            }else{
                installHome = StableUtils.getInstallHome() + File.separator + "scripts";
                File file = new File(installHome);
                if (!file.exists()) {
                    int rv = JOptionPane.showConfirmDialog(
                            null,
                            "您还没有插件商店的资源,是否下载?",
                            Inter.getLocText("FR-Designer-Plugin_Warning"),
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    if (rv == JOptionPane.OK_OPTION) {
                        downloadShopScripts();
                    }
                }
            }
            PluginWebPane webPane = new PluginWebPane(new File(installHome).getAbsolutePath());
            add(webPane, BorderLayout.CENTER);

        } else {
            initTraditionalStore();
        }
    }

    private void initTraditionalStore() {
        UITabbedPane tabbedPane = new UITabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        PluginInstalledPane installedPane = new PluginInstalledPane();
        tabbedPane.addTab(installedPane.tabTitle(), installedPane);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_Update"), new PluginUpdatePane(tabbedPane));
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_All_Plugins"), new PluginFromStorePane(tabbedPane));
    }

    private void downloadShopScripts() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String id = "shop_scripts";
                String username = DesignerEnvManager.getEnvManager().getBBSName();
                String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                try {
                    PluginHelper.downloadPluginFile(id, username, password, new Process<Double>() {
                        @Override
                        public void process(Double integer) {
                        }
                    });
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void done() {
                IOUtils.unzip(new File(StableUtils.pathJoin(PluginHelper.DOWNLOAD_PATH, PluginHelper.TEMP_FILE)), StableUtils.getInstallHome());
                int rv = JOptionPane.showOptionDialog(
                        null,
                        "商店安装完毕,是否立刻启动?",
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
        }.execute();
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}