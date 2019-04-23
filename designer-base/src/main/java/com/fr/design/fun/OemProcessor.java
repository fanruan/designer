package com.fr.design.fun;

import com.fr.design.menu.MenuDef;
import com.fr.start.SplashStrategy;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 设计器Oem接口
 */
public interface OemProcessor {
    public static final String MARK_STRING = "OemProcessor";

    /**
     * 启动动画,如果不替换则返回null
     *
     * @return
     */
    SplashStrategy createSplashStrategy();

    /**
     * 替换标题图标--DesignerFrame.initTitleIcon
     * 如果不替换则返回null
     *
     * @return
     */
    List<BufferedImage> createTitleIcon();

    /**
     * 处理设计器菜单（增删改）
     *
     * @param menuDefs 已加载的菜单
     * @return 新的菜单数组
     */
    MenuDef[] dealWithMenuDef(MenuDef[] menuDefs);

}