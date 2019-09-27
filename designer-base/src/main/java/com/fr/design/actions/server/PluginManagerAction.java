package com.fr.design.actions.server;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.actions.UpdateAction;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.upm.UpmFinder;
import com.fr.design.utils.DesignUtils;
import com.fr.general.IOUtils;
import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 */
public class PluginManagerAction extends UpdateAction {
    private static String PLUGIN_MANAGER_ROUTE = "#management/plugin";
    public PluginManagerAction() {
        this.setMenuKeySet(PLUGIN_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/server/plugin.png"));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 可以启用新版本的插件商店（使用JxBrowser作为容器）
        if(Arch.getArch() == Arch.ARM){
            DesignUtils.visitEnvServerByParameters( PLUGIN_MANAGER_ROUTE,null,null);
            return;
        }
        if (ServerPreferenceConfig.getInstance().isUseOptimizedUPM() && !OperatingSystem.isLinux()) {
            UpmFinder.showUPMDialog();
        } else {
            WebViewDlgHelper.createPluginDialog();
        }
    }

    public static final MenuKeySet PLUGIN_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'I';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}