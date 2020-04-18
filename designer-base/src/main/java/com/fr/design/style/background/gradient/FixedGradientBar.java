package com.fr.design.style.background.gradient;

import java.awt.Color;

/**
 * 这个bar不能拖拽滑动
 */
public class FixedGradientBar extends GradientBar {
    private static final long serialVersionUID = 2787525421995954889L;

    public static final Color NEW_CHARACTER = new Color(99, 178, 238);

    public FixedGradientBar(int minvalue, int maxvalue) {
        super(minvalue, maxvalue);
    }

    @Override
    protected void addMouseDragListener() {
        //不添加拖拽事件
    }
}