package com.fr.design.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

public class FRLeftFlowLayout extends FlowLayout {

	private static final long serialVersionUID = 1L;
	private int firstHgap = 0;
	protected FRLeftFlowLayout(int firstHgap, int hGap, int vGap){
		super(FlowLayout.LEFT, hGap, vGap);	
		this.firstHgap = firstHgap;
	}
	
	 public void layoutContainer(Container target) {
	      synchronized (target.getTreeLock()) {
		Insets insets = target.getInsets();
		int maxwidth = target.getWidth() - (insets.left + insets.right + getHgap()*2);
		int nmembers = target.getComponentCount();
		int x = 0, y = insets.top + getVgap();
		int rowh = 0, start = 0;

	        boolean ltr = target.getComponentOrientation().isLeftToRight();

		for (int i = 0 ; i < nmembers ; i++) {
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
			Dimension d = m.getPreferredSize();
			m.setSize(d.width, d.height);

			if ((x == 0) || ((x + d.width) <= maxwidth)) {
			    if (x > 0) {
				x += getHgap();
			    }
			    x += d.width;
			    rowh = Math.max(rowh, d.height);
			} else {
			    moveComponents(target, insets.left + getHgap(), y, maxwidth - x, rowh, start, i, ltr);
			    x = d.width;
			    y += getVgap() + rowh;
			    rowh = d.height;
			    start = i;
			}
		    }
		}
		moveComponents(target, insets.left + firstHgap, y, maxwidth - x, rowh, start, nmembers, ltr);
	      }
	    }
	
	private void moveComponents(Container target, int x, int y, int width, int height,
            int rowStart, int rowEnd, boolean ltr) {
		synchronized (target.getTreeLock()) {
			switch (getAlignment()) {
			case LEFT:
			    x += ltr ? 0 : width;
			    break;
			case CENTER:
			    x += width / 2;
			    break;
			case RIGHT:
			    x += ltr ? width : 0;
			    break;
			case LEADING:
			    break;
			case TRAILING:
			    x += width;
			    break;
			}
			for (int i = rowStart ; i < rowEnd ; i++) {
			    Component m = target.getComponent(i);
			    if (m.isVisible()) {
			        if (ltr) {
		        	    m.setLocation(x, y + (height - m.getHeight()) / 2);
			        } else {
			            m.setLocation(target.getWidth() - x - m.getWidth(), y + (height - m.getHeight()) / 2);
			        }
			        	x += m.getWidth()+getHgap();
			    }
			}
		      }
	}
}