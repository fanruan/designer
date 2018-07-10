/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.itable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-4-24
 * Time: 上午10:03
 */
public class UIBasicTableUI extends BasicTableUI {

    JTable table;

    public UIBasicTableUI() {
        super();
    }

    public UIBasicTableUI(JComponent table) {
        super();
        this.table = (JTable) table;
        this.table.setRowHeight(20);
    }

    /**
     * 创建UI界面
     * @param table 表
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent table) {
        return new UIBasicTableUI(table);
    }
}