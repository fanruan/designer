package com.fr.design.menu;

import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;
import com.fr.stable.fun.mark.Mutable;

import javax.swing.*;

/**
 * 用来往MenuDef或是ToolBarDef里面加东西时用的接口
 * august:ShortCut没有必要序列化和XMLabled
 * 原来那么多Menudef都提供持久化操作，太浪费资源
 */
@API(level = ShortCut.CURRENT_LEVEL)
public abstract class ShortCut extends AbstractProvider implements Mutable {

    public static final String TEMPLATE_TREE = "TemplateTreeShortCut";

    public static final int CURRENT_LEVEL = 1;

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    private MenuKeySet menuKeySet = null;

    /**
     * 将改菜单添加到menul里面去
     * @param menu 添加到的menu
     */
    public abstract void intoJPopupMenu(JPopupMenu menu);

    /**
     * 将该菜单添加到工具栏里面去
     * @param toolBar 工具栏
     */
    public abstract void intoJToolBar(JToolBar toolBar);


    public abstract void setEnabled(boolean b);

    /**
     * 是否可用
     * @return 可用
     */
    public abstract boolean isEnabled();


    /**
     *作为工具栏菜单
     * @param scs 菜单
     * @return 工具栏
     */
    public static final ToolBarDef asToolBarDef(ShortCut[] scs) {
        ToolBarDef def = new ToolBarDef();
        def.addShortCut(scs);

        return def;
    }


    public MenuKeySet getMenuKeySet() {
        return menuKeySet;
    }

    public void setMenuKeySet(MenuKeySet menuKeySet) {
        this.menuKeySet = menuKeySet;
    }
}