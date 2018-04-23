/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions.help;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: wikky
 * Date: 14-6-20
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
public class SupportQQAction extends UpdateAction {

    public SupportQQAction(){
        this.setMenuKeySet(SUPPORT_QQ);
        this.setName(getMenuKeySet().getMenuName());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/support_qq.png"));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(ProductConstants.SUPPORT_QQ));
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, Inter.getLocText("Set_default_browser"));
            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
        } catch (URISyntaxException exp) {
            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
        }
    }

    public static final MenuKeySet SUPPORT_QQ = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Support_QQ");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}