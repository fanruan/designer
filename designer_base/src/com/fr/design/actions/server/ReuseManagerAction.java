package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.ShopDialog;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.WebManagerPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by vito on 2016/9/27.
 */
public class ReuseManagerAction extends UpdateAction {

    public ReuseManagerAction() {
        this.setMenuKeySet(REUSE_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/server/plugin.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BasicPane managerPane = new WebManagerPaneFactory().createReusePane();
        UIDialog dlg = new ShopDialog(DesignerContext.getDesignerFrame(), managerPane);
        PluginWebBridge.getHelper().setDialogHandle(dlg);
        dlg.setVisible(true);
    }

    public static final MenuKeySet REUSE_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'R';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer-Reuse_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}