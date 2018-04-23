package com.fr.design.gui.itoolbar;

import java.awt.*;

public class UILargeToolbar extends UIToolbar {

	private static final int HEIGHT = 53;
	private static final int WIDTH = 60;

	public UILargeToolbar(int align) {
		super(align);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH ,HEIGHT);
	}
}