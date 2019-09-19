package com.fr.design;

import com.fr.base.vcs.DesignerMode;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-24
 * Time: 上午9:36
 * 记录现在设计器的设计状态
 */
public class DesignState {

    //菜单的几种情况
    //Jwrok

    //worksheet
    public static final int WORK_SHEET = 0;
    //polyDesogner
    public static final int POLY_SHEET = 1;
    //参数面板
    public static final int PARAMETER_PANE = 2;


    //From
    public static final int JFORM = 4;


    //是不是在远程
    public static final int REMOTE = 8;

    //设计状态
    private int designState = -1;
    private boolean isRoot = true;//默认是管理员登陆
    private boolean isAuthority = false;

    public DesignState(ToolBarMenuDockPlus plus) {
        designState = plus.getMenuState();
//        if (WorkContext.getCurrent().isLocal()) {
//            designState += REMOTE;
//        }
//        isRoot = env != null && env.isRoot();
        isAuthority = DesignerMode.isAuthorityEditing();
    }

    public int getDesignState() {
        return designState;
    }

    /**
     * 是否是管理员
     * @return 是管理员返回true
     */
    public boolean isRoot() {
        return this.isRoot;
    }

    /**
     * 是否处于权限编辑状态
     * @return  是则返回true
     */
    public boolean isAuthority() {
        return isAuthority;
    }


}