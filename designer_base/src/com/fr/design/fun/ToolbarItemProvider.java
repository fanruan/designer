package com.fr.design.fun;

import com.fr.form.ui.Widget;
import com.fr.stable.fun.Level;

/**
 * @author : focus
 * @since : 8.0
 * 自定义web工具栏菜单
 */
public interface ToolbarItemProvider extends Level{

    String XML_TAG = "ToolbarItemProvider";

    int CURRENT_LEVEL = 1;


    /**
     * 自定义web工具栏菜单实际类，该类可以继承自com.fr.form.ui.ToolBarMenuButton 或者 com.fr.form.ui.ToolBarButton;
     *
     * @return 菜单类
     */
    Class<? extends Widget> classForWidget();

    /**
     * 自定义web工具栏菜单在设计器界面上的图标路径
     *
     * @return 图标所在的路径
     */
    String iconPathForWidget();

    /**
     * 自定义web工具栏菜单在设计器上显示的名字
     *
     * @return 菜单名
     */
    String nameForWidget();

}