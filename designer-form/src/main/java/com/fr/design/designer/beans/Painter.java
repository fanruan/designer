package com.fr.design.designer.beans;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface Painter {
    //当前焦点区域，即所在容器的边界
    void setRenderingBounds(Rectangle rect);

    //渲染入口，由FormDesigner调用来外成额外渲染
	void paint(Graphics g, int startX, int startY);
}