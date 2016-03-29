package com.fr.design.mainframe.toolbar;

import javax.swing.*;

import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;

public interface ToolBarMenuDockPlus {
    /**
     * 模板的工具
     *
     * @return 工具
     */
	public ToolBarDef[] toolbars4Target();

    /**
     * 文件菜单的子菜单
     *
     * @return 子菜单
     */
	public ShortCut[] shortcut4FileMenu();

    /**
     * 目标的菜单
     *
     * @return 菜单
     */
	public MenuDef[] menus4Target();

    /**
     * 表单的工具栏
     *
     * @return 表单工具栏
     */
	public JPanel[] toolbarPanes4Form();

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
	public JComponent[] toolBarButton4Form();

    /**
     * 权限细粒度状态下的工具面板
     *
     * @return 工具面板
     */
	public JComponent toolBar4Authority();

    public int getMenuState();

    public int getToolBarHeight();

    /**
     * 导出菜单的子菜单 ，目前用于图表设计器
     *
     * @return 子菜单
     */
	public ShortCut[] shortcut4ExportMenu();
	
}