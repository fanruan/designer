package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.menu.MenuKeySet;


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
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/server/plugin.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WebViewDlgHelper.createPluginDialog();
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