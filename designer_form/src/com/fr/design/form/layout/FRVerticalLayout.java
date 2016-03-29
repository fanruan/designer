/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;


/**
 * @author richer
 * @since 6.5.3 垂直布局 所有布局内的控件的宽度将和容器的宽度一样
 */
public class FRVerticalLayout extends FRFlowLayout {

	public static final int TOP = 0;
	public static final int CENTER = 1;
	public static final int BOTTOM = 2;

	public FRVerticalLayout() {
		this(0,0);
	}

	public FRVerticalLayout(int align) {
		this(align,0,0);
	}
	
	public FRVerticalLayout(int hgap, int vgap) {
		this(TOP,hgap,vgap);
	}
	
	public FRVerticalLayout(int alignment, int hgap, int vgap) {
		super(alignment,hgap,vgap);
	}

	@Override
	protected void moveComponents(Container target, Insets insets, int total) {
		int height = target.getHeight() - total - insets.top - insets.bottom;
		int y = insets.top;
		switch (alignment) {
		case TOP:
			y += 0;
			break;
		case CENTER:
			y += height / 2;
			break;
		case BOTTOM:
			y += height;
			break;
		}
		int ex = insets.left + hgap;
		for (int i = 0; i < target.getComponentCount(); i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				m.setLocation(ex, y);
				y += m.getPreferredSize().height + vgap;
			}
		}

	}

	@Override
	protected int reSizeComponents(Container target, Insets insets) {
		int eachWidth = target.getWidth() - insets.left - insets.right - 2 * hgap;
		int total = 0;
		for (int i = 0; i < target.getComponentCount(); i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				m.setSize(eachWidth, m.getPreferredSize().height);
				total += m.getPreferredSize().height + vgap;
			}
		}
		return total;
	}
}