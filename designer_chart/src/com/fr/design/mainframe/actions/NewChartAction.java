/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JChart;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午3:22
 */
public class NewChartAction extends UpdateAction{

    public NewChartAction(){
        this.setMenuKeySet(NEW_CHART);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/newchart_normal.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }


    /**
     * 执行事件
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        DesignerContext.getDesignerFrame().addAndActivateJTemplate(new JChart());
    }

    public static final MenuKeySet NEW_CHART = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M-New_ChartBook");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK);
        }
    };
}