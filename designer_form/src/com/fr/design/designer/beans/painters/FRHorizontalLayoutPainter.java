/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.painters;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.ComponentUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class FRHorizontalLayoutPainter extends FRBoxLayoutPainter {

    public FRHorizontalLayoutPainter(XLayoutContainer container) {
        super(container);
    }

	@Override
	public void paint(Graphics g, int startX, int startY) {
		super.paint(g, startX, startY);
		int x = hotspot.x;
		Rectangle bounds = ComponentUtils.getRelativeBounds(container);
		int my = bounds.y;
		int mx = bounds.x;
		int[] xy = calculateAddPosition(x - mx + startX, 0);
		if (xy.length != 0) {
			drawHotLine(g, startX, startY, xy[0] + mx, my, xy[0] + mx, my + container.getSize().height);
		}
	}
}