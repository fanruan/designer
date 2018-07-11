package com.fr.design.fun.impl;

import com.fr.design.fun.MenuHandler;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
@API(level = MenuHandler.CURRENT_LEVEL)
public abstract class AbstractMenuHandler extends AbstractProvider implements MenuHandler {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }

    /**
     * 获取当前菜单对应的Action
     * 不需要选中对象, (文件, 服务器, 关于)
     *
     * @return 菜单Action
     */
    public ShortCut shortcut() {
        return null;
    }

    /**
     * 获取当前菜单对应的Action
     *
     * @param plus 当前选中的对象(模板)
     * @return 菜单Action
     */
    public ShortCut shortcut(ToolBarMenuDockPlus plus) {
        return null;
    }
}