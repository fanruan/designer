package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import javafx.embed.swing.JFXPanel;

import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by vito on 2016/9/28.
 */
public class WebManagerPaneFactory {
    private String installHome;

    public WebManagerPaneFactory() {
        if (StableUtils.isDebug()) {
            URL url = ClassLoader.getSystemResource("");
            this.installHome = url.getPath();
        } else {
            this.installHome = StableUtils.getInstallHome();
        }
    }

    public BasicPane createPluginPane() {
        if (StableUtils.getMajorJavaVersion() == 8) {
            return new ShopManagerPane(new ShopPaneConfig() {
                @Override
                String setMainJS() {
                    String relativePath = "/scripts/store/web/index.html";
                    return StableUtils.pathJoin(new File(installHome).getAbsolutePath(), relativePath);
                }

                @Override
                String setScriptsId() {
                    return "shop_scripts";
                }

                @Override
                JFXPanel setWebPane() {
                    return new PluginWebPane(setMainJS());
                }

            });
        } else {
            BasicPane traditionalStorePane = new BasicPane() {
                @Override
                protected String title4PopupWindow() {
                    return Inter.getLocText("FR-Designer-Plugin_Manager");
                }
            };
            traditionalStorePane.setLayout(new BorderLayout());
            traditionalStorePane.add(initTraditionalStore(), BorderLayout.CENTER);
            return traditionalStorePane;
        }
    }

    public BasicPane createReusePane() {
        return new ShopManagerPane(new ShopPaneConfig() {
            @Override
            String setMainJS() {
                String relativePath = "/scripts/store/reuse/index.html";
                return StableUtils.pathJoin(new File(installHome).getAbsolutePath(), relativePath);
            }

            @Override
            String setScriptsId() {
                return "reuse_scripts";
            }

            @Override
            JFXPanel setWebPane() {
                return new ReuseWebPane(setMainJS());
            }
        });
    }

    /**
     * 以关键词打开设计器商店
     *
     * @param keyword 关键词
     */
    public BasicPane createPluginPane(String keyword) {
        PluginWebBridge.getHelper().openWithSearch(keyword);
        return createPluginPane();
    }

    private Component initTraditionalStore() {
        UITabbedPane tabbedPane = new UITabbedPane();
        PluginInstalledPane installedPane = new PluginInstalledPane();
        tabbedPane.addTab(installedPane.tabTitle(), installedPane);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_Update"), new PluginUpdatePane(tabbedPane));
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_All_Plugins"), new PluginFromStorePane(tabbedPane));
        return tabbedPane;
    }
}
