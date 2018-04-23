/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.data.datapane.connect;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.controlpane.NameObjectCreator;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-25
 * Time: 下午5:05
 */
public abstract class DataConnectionAction extends UpdateAction {
    public static final String XML_TAG = "connectionType";

    public DataConnectionAction() {
        this.setName(this.getDisplayName());
        this.setSmallIcon(BaseUtils.readIcon(this.getIconPath()));
    }

    /**
     * 名字
     *
     * @return
     */
    public abstract String getDisplayName();

    /**
     * 图标
     *
     * @return
     */
    public abstract String getIconPath();

    /**
     * 得到数据连接的声明类
     *
     * @return
     */
    public abstract Class getConnectionClass();

    /**
     * 得到相应面板的类
     *
     * @return
     */
    public abstract Class getUpdateConnectionPaneClass();


    public NameObjectCreator getConnectionCreator() {
        return new NameObjectCreator(getDisplayName(), getIconPath(), getConnectionClass(), getUpdateConnectionPaneClass());
    }

    /**
     * 动作
     * @param e 动作
     */
    public void actionPerformed(ActionEvent e) {
    }
}