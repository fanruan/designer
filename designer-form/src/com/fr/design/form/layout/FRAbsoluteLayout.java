/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-25
 */
public class FRAbsoluteLayout implements FRLayoutManager {

	@Override
	public void addLayoutComponent(String name, Component comp) {

	}

	@Override
	public void removeLayoutComponent(Component comp) {

	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(10, 10);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(10, 10);
	}

	@Override
	public void layoutContainer(Container parent) {

	}

	@Override
	public boolean isResizable() {
		return true;
	}
}