/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.ibutton;

import com.fr.design.gui.imenu.UIBasicMenuItemUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-6
 * Time: 下午4:12
 */
public class UIRadioButtonMenuItemUI extends UIBasicMenuItemUI {

    /**
     * 创建UI
     *
     * @param b 组件
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent b) {
        return new UIRadioButtonMenuItemUI();
    }

    protected String getPropertyPrefix() {
        return "RadioButtonMenuItem";
    }

    /**
     * 鼠标点击事件
     *
     * @param item    菜单项
     * @param e       鼠标事件
     * @param path    菜单元素路径
     * @param manager 鼠标选择manager
     */
    public void processMouseEvent(JMenuItem item, MouseEvent e,
                                  MenuElement path[], MenuSelectionManager manager) {
        Point p = e.getPoint();
        boolean isContainsX = p.x >= 0 && p.x < item.getWidth();
        boolean isContainsY = p.y >= 0 && p.y < item.getHeight();
        if (isContainsX && isContainsY) {
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                manager.clearSelectedPath();
                item.doClick(0);
                item.setArmed(false);
            } else
                manager.setSelectedPath(path);
        } else if (item.getModel().isArmed()) {
            MenuElement[] newPath = new MenuElement[path.length - 1];
            int i, c;
            for (i = 0, c = path.length - 1; i < c; i++){
                newPath[i] = path[i];
            }
            manager.setSelectedPath(newPath);
        }
    }

}