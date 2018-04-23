package com.fr.design.designer.beans.painters;

import java.awt.Color;
import java.awt.Graphics;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XLayoutContainer;

public class NullLayoutPainter extends AbstractPainter {

    public NullLayoutPainter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public void paint(Graphics g,int startX,int startY) {
    	
        HoverPainter painter = container.getLayoutAdapter().getPainter();
        if (painter != null) {
            painter.setCreator(this.creator);
            painter.setHotspot(this.hotspot);
            painter.setRenderingBounds(this.hotspot_bounds);
            painter.paint(g,startX,startY);
        } else {
            g.setColor(Color.lightGray);
            g.drawRect(hotspot_bounds.x, hotspot_bounds.y, hotspot_bounds.width, hotspot_bounds.height);
        }
    }
}