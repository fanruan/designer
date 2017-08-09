package com.fr.plugin.chart.map.server;

import com.fr.design.fun.impl.AbstractMenuHandler;
import com.fr.design.menu.ShortCut;

/**
 * Created by Mitisky on 16/4/29.
 */
public class MapMenuHandler extends AbstractMenuHandler {
    private static final int POSITION_OFFSET = 11;

    /**
     * 插入菜单的位置
     *
     * @param total 插入的位置
     * @return 插入位置，如果想放到最后，则返回-1
     */
    @Override
    public int insertPosition(int total) {
        return POSITION_OFFSET;
    }

    /**
     * 是否在插入的菜单前插入一个分割符
     *
     * @return 是否插入分隔符
     */
    @Override
    public boolean insertSeparatorBefore() {
        return false;
    }

    /**
     * 是否在插入的菜单后插入一个分割符
     *
     * @return 是否插入分隔符
     */
    @Override
    public boolean insertSeparatorAfter() {
        return false;
    }

    /**
     * 所属的分类菜单
     *
     * @return 分类菜单名
     */
    @Override
    public String category() {
        return SERVER;
    }

    /**
     * 快捷方式
     * @return 生成一个实例
     */
    @Override
    public ShortCut shortcut() {
        return new ChartMapEditorAction();
    }
}
