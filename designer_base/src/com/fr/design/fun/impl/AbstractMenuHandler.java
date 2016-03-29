package com.fr.design.fun.impl;

import com.fr.design.fun.MenuHandler;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.general.ComparatorUtils;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractMenuHandler implements MenuHandler {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


    public boolean equals(Object obj) {
        return obj instanceof AbstractMenuHandler
                && ComparatorUtils.equals(category(), ((AbstractMenuHandler) obj).category())
                && shortCutEquals(this, (AbstractMenuHandler)obj);
    }

    private boolean shortCutEquals(AbstractMenuHandler target, AbstractMenuHandler self){
        ShortCut targetShortCut = target.shortcut();
        ShortCut selfShortCut = self.shortcut();

        if (targetShortCut == null && selfShortCut == null){
            return true;
        }

        if (targetShortCut != null && selfShortCut != null){
            return ComparatorUtils.equals(targetShortCut.getClass(), selfShortCut.getClass());
        }

        return false;
    }

    /**
     * 获取当前菜单对应的Action
     * 不需要选中对象, (文件, 服务器, 关于)
     *
     * @return 菜单Action
     *
     */
    public ShortCut shortcut(){
        return null;
    }

    /**
     * 获取当前菜单对应的Action
     *
     * @param plus 当前选中的对象(模板)
     *
     * @return 菜单Action
     *
     */
    public ShortCut shortcut(ToolBarMenuDockPlus plus){
        return null;
    }
}