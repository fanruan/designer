package com.fr.design.designer.beans.painters;

import com.fr.design.designer.creator.XLayoutContainer;

import java.awt.*;

/**
 * Created by zhouping on 2016/7/11.
 */
public class FRAbsoluteLayoutPainter extends AbstractPainter {
    public FRAbsoluteLayoutPainter(XLayoutContainer container) {
        super(container);
    }

    /**
     * 组件渲染
     *
     * @param g      画图类
     * @param startX 开始位置x
     * @param startY 开始位置y
     */
    @Override
    public void paint(Graphics g, int startX, int startY) {
        super.paint(g, startX, startY);
    }
}
