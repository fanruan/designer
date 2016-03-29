package com.fr.design.fun;

import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.stable.fun.Level;

/**
 * @author richie
 * @date 2015-04-01
 * @since 8.0
 * 设计器菜单栏插件接口
 */
public interface MenuHandler extends Level{

    String MARK_STRING = "MenuHandler";

    int CURRENT_LEVEL = 1;


    int LAST = -1;

    String HELP = "help";
    String SERVER = "server";
    String FILE = "file";
    String TEMPLATE = "template";
    String INSERT = "insert";
    String CELL = "cell";

    /**
     * 插入菜单的位置
     *
     * @param total 插入的位置
     *
     * @return 插入位置，如果想放到最后，则返回-1
     */
    int insertPosition(int total);

    /**
     * 是否在插入的菜单前插入一个分割符
     * @return 是否插入分隔符
     */
    boolean insertSeparatorBefore();

    /**
     * 是否在插入的菜单后插入一个分割符
     * @return 是否插入分隔符
     */
    boolean insertSeparatorAfter();

    /**
     * 所属的分类菜单
     * @return 分类菜单名
     */
    String category();

    /**
     * 具体的菜单项内容
     * @return 菜单项内容
     */
    ShortCut shortcut();

    /**
     * 具体的菜单项内容
     * @param plus 当前模板
     *
     * @return 菜单项内容
     */
    ShortCut shortcut(ToolBarMenuDockPlus plus);

    /**
     * 两个菜单项相等等情况
     * @param obj 比较对象
     * @return 相等则返回true，否则返回false
     */
    boolean equals(Object obj);
}