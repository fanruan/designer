package com.fr.design.menu;

import com.fr.design.DesignState;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-23
 * Time: 下午5:25
 */
public class MenuManager {

    private static MenuManager menuManager = null;
    //文件、模板、插入、单元格、服务器、帮助六大菜单
    public static final int FILE = 0;
    public static final int TEMPLATE = 1;
    private static final int INSERT = 2;
    private static final int CELL = 3;
    private static final int SERVER = 4;
    public static final int HELP = 5;
    private static final boolean[] DEFAULT_TOP_MENUS = new boolean[]{true, true, true, true, true, true};

    public synchronized static MenuManager getInstance() {
        if (menuManager == null) {
            menuManager = new MenuManager();
        }
        return menuManager;
    }

    private MenuManager() {

    }

    //菜单的16种情况
    public void setMenus4Designer(DesignState state) {
        boolean[] topMenuVisibleGroup = DEFAULT_TOP_MENUS;
        int designSate = state.getDesignState();
        boolean isFormLayout = designSate == DesignState.PARAMETER_PANE || designSate == DesignState.JFORM;
        if (isFormLayout || (designSate == DesignState.POLY_SHEET)) {
            topMenuVisibleGroup[INSERT] = false;
            topMenuVisibleGroup[CELL] = false;
        }

        //权限编辑状态下，将插入和单元格菜单屏蔽
        if (state.isAuthority()) {
            topMenuVisibleGroup[INSERT] = false;
            topMenuVisibleGroup[CELL] = false;
        }


        //普通用户登录，讲服务器屏蔽
        topMenuVisibleGroup[SERVER] = state.isRoot();

    }
}