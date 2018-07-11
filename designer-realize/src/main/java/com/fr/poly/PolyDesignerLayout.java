package com.fr.poly;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import com.fr.general.ComparatorUtils;

public class PolyDesignerLayout implements LayoutManager, java.io.Serializable {
	public static final String Center = "Center";
	public static final String Vertical = "Vertical";
	public static final String HRuler = "HRuler";
	public static final String VRuler = "VRuler";

	private Component designer;
	private Component verScrollBar;
	private Component hRuler;
	private Component vRuler;

	public PolyDesignerLayout() {
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (Center.equals(name)) {
			designer = comp;
		} else if (Vertical.equals(name)) {
			verScrollBar = comp;
		} else if (HRuler.equals(name)) {
			hRuler = comp;
		} else if (VRuler.equals(name)) {
			vRuler = comp;
		} 
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (ComparatorUtils.equals(comp, designer)) {
			designer = null;
		}  else if (ComparatorUtils.equals(comp, verScrollBar)) {
			verScrollBar = null;
		} else if (ComparatorUtils.equals(comp, hRuler)) {
			hRuler = null;
		} else if (ComparatorUtils.equals(comp, vRuler)) {
			vRuler = null;
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension();
			Dimension dim0 = designer.getPreferredSize();
			dim.width += dim0.width;
			dim.height += dim0.height;
			Dimension dim2 = verScrollBar.getPreferredSize();
			dim.width += dim2.width;
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension();
			Dimension dim0 = designer.getMinimumSize();
			dim.width += dim0.width;
			dim.height += dim0.height;
			Dimension dim2 = verScrollBar.getMinimumSize();
			dim.width += dim2.width;
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}
	}

	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int bottom = target.getHeight() - insets.bottom;
			int left = insets.left;
			int right = target.getWidth() - insets.right;
			Dimension hRulerPreferredSize = hRuler == null ? new Dimension() :hRuler.getPreferredSize();
			Dimension vRulerPreferredSize = vRuler == null ? new Dimension() :vRuler.getPreferredSize();
//			Dimension hbarPreferredSize = horScrollBar.getPreferredSize();
			Dimension vbarPreferredSize = verScrollBar.getPreferredSize();
			if(hRuler != null) {
				hRuler.setBounds(left + vRulerPreferredSize.width -1, top, right - vRulerPreferredSize.width, hRulerPreferredSize.height);
			}
			if(vRuler != null) {
				vRuler.setBounds(left, top + vRulerPreferredSize.height -1, vRulerPreferredSize.width, bottom - hRulerPreferredSize.height);
			}
//			horScrollBar.setBounds(left + vRulerPreferredSize.height, bottom - hbarPreferredSize.height, right - vRulerPreferredSize.height - 16, hbarPreferredSize.height);
			verScrollBar.setBounds(right - vRulerPreferredSize.width, top + vRulerPreferredSize.height, vbarPreferredSize.width, bottom - hRulerPreferredSize.height - 16);
			
			designer.setBounds(
					left + vRulerPreferredSize.width, 
					top + vRulerPreferredSize.height, 
					right - vbarPreferredSize.width - vRulerPreferredSize.width,
					bottom );
		}
	}
}