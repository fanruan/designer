package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

public abstract class FRSplitLayout implements FRLayoutManager, LayoutManager2, java.io.Serializable {
	public static final String CENTER = "center";
	public static final String ASIDE = "aside";
	// 分割比率
	protected double ratio;
	protected int hgap;
	protected int vgap;

	protected Component center;
	protected Component aside;

	public FRSplitLayout() {
		this(0.5);
	}

	public FRSplitLayout(double ratio) {
		this(ratio, 0, 0);
	}

	public FRSplitLayout(double ratio, int hgap, int vgap) {
		this.ratio = ratio;
		this.hgap = hgap;
		this.vgap = vgap;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		if (ratio < 0 || ratio > 1) {
			throw new IllegalArgumentException("Ratio must be in [0, 1]");
		}
		this.ratio = ratio;
	}

	public int getHgap() {
		return hgap;
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	public int getVgap() {
		return vgap;
	}

	public void setVgap(int vgap) {
		this.vgap = vgap;
	}

	public Object getConstraints(Component comp) {
		if (comp == center) {
			return CENTER;
		} else if (comp == aside) {
			return ASIDE;
		} else {
			return null;
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		synchronized (comp.getTreeLock()) {
			if (constraints == null || constraints instanceof String) {
				addLayoutComponent((String) constraints, comp);
			}
		}
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
			if (name == null) {
				name = CENTER;
			}
			if (CENTER.equals(name)) {
				center = comp;
			} else if (ASIDE.equals(name)) {
				aside = comp;
			}
		}
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		synchronized (comp.getTreeLock()) {
			if (comp == center) {
				center = null;
			} else if (comp == aside) {
				aside = null;
			}
		}
	}

	public Component getLayoutComponent(Object constraints) {
		if (CENTER.equals(constraints)) {
			return center;
		} else if (ASIDE.equals(constraints)) {
			return aside;
		} else {
			throw new IllegalArgumentException("error " + constraints + "!");
		}
	}

	@Override
	public boolean isResizable() {
		return false;
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension asideDim = null;
			if (aside != null) {
				asideDim = aside.getPreferredSize();
			}
			Dimension centerDim = null;
			if (center != null) {
				centerDim = center.getPreferredSize();
			}
			return calculateAppropriateSize(target, asideDim, centerDim);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension asideDim = null;
			if (aside != null) {
				asideDim = aside.getMinimumSize();
			}
			Dimension centerDim = null;
			if (center != null) {
				centerDim = center.getMinimumSize();
			}
			return calculateAppropriateSize(target, asideDim, centerDim);
		}
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension asideDim = null;
			if (aside != null) {
				asideDim = aside.getMaximumSize();
			}
			Dimension centerDim = null;
			if (center != null) {
				centerDim = center.getMaximumSize();
			}
			return calculateAppropriateSize(target, asideDim, centerDim);
		}
	}

	protected abstract Dimension calculateAppropriateSize(Container target, Dimension asideDim, Dimension centerDim);

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {

	}
}