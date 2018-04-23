package com.fr.design.actions.tabledata;

import com.fr.base.BaseUtils;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.actions.UpdateAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-19
 * Time: 下午5:21
 */
public abstract class TableDataAction extends UpdateAction {
    public static final String XML_TAG_TEMPLATE = "template_tabledata_Type";
    public static final String XML_TAG_SERVER = "server_tabledata_Type";

    public TableDataAction() {
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
     * 后缀
     *
     * @return
     */
    public abstract String getPrefix();


    /**
     * 得到TableData的声明类
     *
     * @return
     */
    public abstract Class getTableDataClass();

    /**
     * 得到相应面板的类
     *
     * @return
     */
    public abstract Class getUpdateTableDataPaneClass();

    /**
     * 用于初始化的类。诺是不需要初始化，则不需要实现
     *
     * @return
     */
    public Class getClass4Init() {
        return null;
    }

    /**
     * 该菜单后面是否需要插入分割符
     *
     * @return 是则返回true
     */
    public boolean isNeedInsertSeparator() {
        return false;
    }

    public TableDataNameObjectCreator getTableDataCreator() {
        if (this.getClass4Init() == null) {
            return new TableDataNameObjectCreator(this);
        } else {
            return new TableDataNameObjectCreator(this, getClass4Init());
        }
    }


    /**
     * 动作
     * @param e 动作
     */
    public void actionPerformed(ActionEvent e) {
    }


}