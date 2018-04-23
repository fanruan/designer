/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.help;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * help about.
 */
public class AboutAction extends UpdateAction {
    public AboutAction() {
        this.setMenuKeySet(ABOUT);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
    }

    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        AboutPane aboutPane = new AboutPane();
        AboutDialog aboutDailog = new AboutDialog(DesignerContext.getDesignerFrame(), aboutPane);
        aboutDailog.setVisible(true);
    }

    public static final MenuKeySet ABOUT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Help-About_Software");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };



}