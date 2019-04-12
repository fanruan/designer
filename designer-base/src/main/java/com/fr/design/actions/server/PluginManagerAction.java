package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.ShopDialog;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.upm.UPM;
import com.fr.design.upm.UPMDialog;
import com.fr.design.upm.UPMPane;
import com.fr.general.IOUtils;


import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 */
public class PluginManagerAction extends UpdateAction {

    public PluginManagerAction() {
        this.setMenuKeySet(PLUGIN_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/server/plugin.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 可以启用新版本的插件商店（使用JxBrowser作为容器）
        if (DesignerEnvManager.getEnvManager().isOpenDebug()
                ||ServerPreferenceConfig.getInstance().isUseOptimizedUPM()) {
            UPM.showUPMDialog();
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