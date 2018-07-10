/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui;

import com.fr.design.mainframe.chart.gui.other.ChartDesignerConditionAttrPane;
import com.fr.design.mainframe.chart.gui.other.ChartDesignerInteractivePane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * 图表设计器的图标属性表高级tab
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午2:04
 */
public class ChartDesignerOtherPane extends ChartOtherPane {

    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        otherPane = new ChartTabPane();
        content.add(otherPane, BorderLayout.CENTER);
        return content;
    }

    /**
     * 界面标题
     *
     * @return 返回标题.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Advanced");
    }

    private boolean isHaveCondition() {
        return hasCondition;
    }


    private class ChartTabPane extends TabPane {

        @Override
        protected java.util.List<BasicPane> initPaneList() {
            java.util.List<BasicPane> paneList = new ArrayList<BasicPane>();
            interactivePane = new ChartDesignerInteractivePane(ChartDesignerOtherPane.this);

            paneList.add(interactivePane);

            if (ChartDesignerOtherPane.this.isHaveCondition()) {
                conditionAttrPane = new ChartDesignerConditionAttrPane();
                paneList.add(conditionAttrPane);
            }
            return paneList;
        }

    }
}