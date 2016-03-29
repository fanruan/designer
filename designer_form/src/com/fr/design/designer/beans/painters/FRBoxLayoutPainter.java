/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.painters;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class FRBoxLayoutPainter extends AbstractPainter {

    public FRBoxLayoutPainter(XLayoutContainer container) {
        super(container);
    }

    protected int[] calculateAddPosition(int x, int y) {
        int[] result = new int[2];
        int count = container.getComponentCount();
        for (int i = 0; i < count; i++) {
            Component c1 = container.getComponent(i);
            if(i == 0) {
            	result[0] = c1.getBounds().x;
            	result[1] = c1.getBounds().y;
            }
            Component c2 = null;
            if (i < count - 1) {
                c2 = container.getComponent(i + 1);
            }
            if (c2 != null) {
                if (x > c1.getBounds().x && x < c2.getBounds().x) {
                    result[0] = c1.getBounds().x + c1.getBounds().width;
                } else if (x <= c1.getBounds().x && result[0] > c1.getBounds().x) {
                    result[0] = c1.getBounds().x;
                }
                if (y > c1.getBounds().y && y < c2.getBounds().y) {
                    result[1] = c1.getBounds().y + c1.getSize().height;
                } else if (y <= c1.getBounds().y && result[1] > c1.getBounds().y) {
                    result[1] = c1.getBounds().y;
                }
            } else {
                if (x > c1.getBounds().x) {
                    result[0] = c1.getBounds().x + c1.getBounds().width;
                }
                if (y > c1.getBounds().y) {
                    result[1] = c1.getBounds().y + c1.getSize().height;
                }
            }
        }
        return result;
    }
    
	protected void drawHotLine(Graphics g, int startX, int startY, int x1, int y1, int x2, int y2) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(XCreatorConstants.LAYOUT_HOTSPOT_COLOR);
		g2d.setStroke(XCreatorConstants.STROKE);
		g2d.drawLine(x1 - startX, y1 - startY, x2 - startY, y2 - startY);
	}
}