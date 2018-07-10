/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions;

import com.fr.base.BaseUtils;
import com.fr.stable.StringUtils;


/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-28
 * Time: 上午9:44
 */
public abstract class MenuAction extends UpdateAction {

    public MenuAction() {
        this.setName(getDisplayName());
        this.setMnemonic(getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/bindColumn.png"));
    }

    /**
     * 名字
     *
     * @return
     */
    public abstract String getDisplayName();

    public char getMnemonic() {
        return '\0';
    }

    /**
     * 图标
     *
     * @return
     */
    public String getIconPath() {
        return StringUtils.EMPTY;
    }

}