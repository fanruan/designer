package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public abstract class FRFlowLayout implements LayoutManager, FRLayoutManager, java.io.Serializable {

	protected int hgap;
	protected int vgap;
	protected int alignment;

	public FRFlowLayout() {
		this(0, 0);
	}

	public FRFlowLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}

	public FRFlowLayout(int align, int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;

		setAlignment(align);
	}
	
	public void setHgap(int gap) {
		this.hgap = gap;
	}
	
	public void setVgap(int gap) {
		this.vgap = gap;
	}

	public void setAlignment(int align) {
		this.alignment = align;
	}

	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			moveComponents(target, insets, reSizeComponents(target, insets));
		}
	}

	protected abstract int reSizeComponents(Container target, Insets insets);

	protected abstract void moveComponents(Container target, Insets insets, int total);

	@Override
	public void addLayoutComponent(String name, Component comp) {

	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return null;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return parent.getPreferredSize();
	}

	@Override
	public void removeLayoutComponent(Component comp) {

	}
	
	@Override
	public boolean isResizable() {
		return false;
	}

}