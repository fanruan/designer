/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import javax.swing.Icon;

import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.dialog.BasicPane;

/**
 * @author richer
 * @since 6.5.5
 *        创建于
 *        所有的用于Docking的View里面的面板都继承自此类
 *        此类的作用是在任何时候打开该Docking的时候都能和相应的设计界面关联起来
 */
// TODO ALEX_SEP 有四个位置可以放Docking,能不能把该Docking的Preferred的位置属性放在Docking里面呢?
public abstract class DockingView extends BasicPane {
    /**
     * Generally speaking, invoke this method when need refresh the content of the docking.
     * @param agrs
     */
    // TODO ALEX_SEP 这个方法不想传任何参数
    public abstract void refreshDockingView();

    public abstract String getViewTitle();

    public abstract Icon getViewIcon();
    
    public abstract Location preferredLocation();
    
    public UITitlePanel createTitlePanel(){
    	return new UITitlePanel(this,getViewTitle());
    }
    
    @Override
    protected String title4PopupWindow() {
    	return getViewTitle();
    }
    
    public static enum Location {
    	WEST_ABOVE, WEST_BELOW
    }
}