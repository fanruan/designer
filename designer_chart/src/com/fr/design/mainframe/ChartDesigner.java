/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;
import com.fr.form.ui.ChartBook;
import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午4:30
 */
public class ChartDesigner extends TargetComponent<ChartBook>  implements MouseListener{

    private ChartArea chartArea;//上层区域
    private boolean hasCalGap = false;
    private ChartDesignerUI designerUI;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private ChartToolBarPane chartToolBarPane;

    public ChartDesigner(ChartBook chartBook) {
        super(chartBook);
        this.addMouseListener(this);
        designerUI = new ChartDesignerUI();
        chartToolBarPane = new ChartToolBarPane(this){
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                return new Dimension(size.width, ChartToolBarPane.TOTAL_HEIGHT);
            }
        };
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(designerUI!=null){
                    designerUI.mouseMoved(e);
                    ChartDesigner.this.repaint();
                }
            }
        });
        updateUI();// 初始化界面设计工具的UI实例
    }

    /**
     * 设置其UI类为DesignerUI，负责渲染
     */
    @Override
    public void updateUI() {
        setUI(designerUI);
    }

    /**
     * 设置上层区域
     * @param chartArea 图表区域
     */
    public void setParent(ChartArea chartArea) {
        this.chartArea = chartArea;
    }

    /**
     * 复制
     */
    public void copy() {

    }

    /**
     * 黏贴
     * @return 成功返回true
     */
    public boolean paste() {
        return false;
    }

    /**
     * 剪切
     * @return 成功返回TRUE
     */
    public boolean cut() {
        return false;
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {

    }

    /**
     * 权限编辑面板
     * @return 面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return null;
    }

    /**
     * 工具条
     * @return 工具条
     */
    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return null;
    }

    /**
     * 菜单状态
     * @return 状态
     */
    public int getMenuState() {
        return 0;
    }

    /**
     * 东上面板
     * @return 面板
     */
    public JPanel getEastUpPane() {
        return null;
    }

    /**
     * 东下面板
     * @return 面板
     */
    public JPanel getEastDownPane() {
        return null;
    }

    /**
     * 取消格式
     */
    public void cancelFormat() {

    }

    /**
     * 图表设计器得工具条项
     * @return 图表设计器得工具条项
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * 菜单
     * @return 菜单
     */
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    /**
     * 菜单项
     * @return 菜单项
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * 权限编辑得菜单项
     * @return 菜单项
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    /**
     * 表单得工具条按钮
     * @return 表单得工具条按钮
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }


    /**
     * 返回表单区域
     * @return 表单区域
     */
    public ChartArea getArea() {
        return chartArea;
    }


    /**
     * 鼠标点击
     * @param e 事件
     */
    public void mouseClicked(MouseEvent e) {
        designerUI.mouseClicked(e);
        this.chartToolBarPane.populate();
    }

    /**
     * 鼠标按下
     * @param e 事件
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * 鼠标释放
     * @param e 事件
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * 鼠标进入
     * @param e 事件
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * 鼠标退出
     * @param e 事件
     */
    public void mouseExited(MouseEvent e) {

    }


    private void registerChangeListener(ChangeListener changeListener){
        if(changeListener == null){
            return;
        }
        this.changeListeners.add(changeListener);
    }

    public ChartToolBarPane getChartToolBarPane(){
        return this.chartToolBarPane;
    }

    public void populate(){
        this.chartToolBarPane.populate();
    }

    /**
     * 清除工具栏上面全局风格按钮的选中
     */
    public void clearToolBarStyleChoose(){
        chartToolBarPane.clearStyleChoose();
    }
}