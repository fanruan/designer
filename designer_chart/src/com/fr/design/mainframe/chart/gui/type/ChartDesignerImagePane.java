/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.BaseUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * 图表设计器，工具栏上的图表类型选择用的单个小图面板
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午4:24
 */
public class ChartDesignerImagePane extends JPanel implements MouseListener{

    private static final int SIZE = 28;
    private static final String NOMAL = "normal";
    private static final String OVER = "over";
    private static final String PRESS = "normal";

    private String iconPath;
    private int chartType;
    private String state = NOMAL;//状态，按下、悬浮、正常
    private Icon mode;
    private boolean isSelected;
    private PlotPane4ToolBar parent;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public ChartDesignerImagePane(String iconPath, int chartType, String tipName,PlotPane4ToolBar parent) {
        this.iconPath = iconPath;
        this.chartType = chartType;
        this.isSelected = false;
        addMouseListener(this);
        this.setToolTipText(tipName);
        this.parent = parent;
    }

    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        mode = BaseUtils.readIcon(iconPath + chartType + "_" + state + ".png");
        if(this.isSelected){
            Icon border =BaseUtils.readIcon("com/fr/design/images/toolbar/border.png");
            border.paintIcon(this,g,0,0);
        }
        mode.paintIcon(this, g, 3, 3);
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.state = isSelected ? PRESS : NOMAL;
    }


    /**
     * 鼠标点击
     * @param e 事件
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * 鼠标按下
     * @param e 事件
     */
    public void mousePressed(MouseEvent e) {
        parent.clearChoose();
        if(parent.getSelectedIndex() != this.chartType){
            parent.setSelectedIndex(this.chartType);
            this.fireStateChange();
        }
        this.isSelected = true;
        state = PRESS;
        this.repaint();
    }

    /**
     * 鼠标释放
     * @param e 事件
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * 鼠标点击
     * @param e 进入
     */
    public void mouseEntered(MouseEvent e) {
        if (this.isSelected) {
            state = PRESS;
        } else {
            state = OVER;
        }
        this.repaint();
    }

    /**
     * 鼠标点击
     * @param e 离开
     */
    public void mouseExited(MouseEvent e) {
        if (this.isSelected) {
            state = PRESS;
        } else {
            state = NOMAL;
        }
        this.repaint();
    }

    private void fireStateChange() {
   		for (int i = 0; i < changeListeners.size(); i++) {
   			changeListeners.get(i).stateChanged(new ChangeEvent(this));
   		}
   	}

    /**
     * 注册事件监听
     * @param listener  监听
     */
    public void registeChangeListener(ChangeListener listener){
        changeListeners.add(listener);
    }

}