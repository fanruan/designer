package com.fr.design.scrollruler;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import com.fr.general.ComparatorUtils;

public class RulerLayout implements LayoutManager, java.io.Serializable {
	public static final String CENTER = "Center";
	public static final String HIRIZONTAL = "Horizontal";
	public static final String VERTICAL = "Vertical";
	public static final String HRULER = "HRuler";
	public static final String VRULER = "VRuler";
	public static final String BOTTOM = "Bottom";

	protected Component designer;
    protected Component horScrollBar;
    protected Component verScrollBar;
    protected Component hRuler;
    protected Component vRuler;
    protected Component resizePane;
    protected int BARSIZE = 16;

	public RulerLayout() {
	}

    /**
     * 容器增加组件
     * 
     * @param name   组件名
     * @param comp   组 件
     */
	public void addLayoutComponent(String name, Component comp) {
		if (ComparatorUtils.equals(name, CENTER)) {
			designer = comp;
		} else if (ComparatorUtils.equals(name, HIRIZONTAL)) {
			horScrollBar = comp;
		} else if (ComparatorUtils.equals(name, VERTICAL)) {
			verScrollBar = comp;
		} else if (ComparatorUtils.equals(name, HRULER)) {
			hRuler = comp;
		} else if (ComparatorUtils.equals(name, VRULER)) {
			vRuler = comp;
		} else if (ComparatorUtils.equals(name, BOTTOM)) {
			resizePane = comp;
		}
	}

    /**
     * 去除组件
     * 
     * @param comp     组件
     */
	public void removeLayoutComponent(Component comp) {
		if (ComparatorUtils.equals(comp, designer)) {
			designer = null;
		} else if (ComparatorUtils.equals(comp, horScrollBar)) {
			horScrollBar = null;
		} else if (ComparatorUtils.equals(comp, verScrollBar)) {
			verScrollBar = null;
		} else if (ComparatorUtils.equals(comp, hRuler)) {
			hRuler = null;
		} else if (ComparatorUtils.equals(comp, vRuler)) {
			vRuler = null;
		} else if (ComparatorUtils.equals(comp, resizePane)) {
			resizePane = null;
		}
	}

    /**
     * 适合尺寸
     * 
     * @param target     容器
     * @return    尺寸
     */
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension();
			Dimension dim0 = designer.getPreferredSize();
			dim.width += dim0.width;
			dim.height += dim0.height;
			dim.height += horScrollBar == null ? 0 : horScrollBar.getPreferredSize().height;
			dim.width += verScrollBar == null ? 0 : verScrollBar.getPreferredSize().width;
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}
	}

    /**
     * 计算最小尺寸
     * @param target     容器
     * @return 尺寸
     */
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension();
			Dimension dim0 = designer.getMinimumSize();
			dim.width += dim0.width;
			dim.height += dim0.height;
			Dimension dim1 = horScrollBar.getMinimumSize();
			dim.height += dim1.height;
			Dimension dim2 = verScrollBar.getMinimumSize();
			dim.width += dim2.width;
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}
	}

    /**
     * 布局管理器重新调整内部组件位置
     * 
     * @param target    容器
     */
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int bottom = target.getHeight() - insets.bottom;
			int left = insets.left;
			int right = target.getWidth() - insets.right;
			Dimension hRulerPreferredSize = hRuler == null ? new Dimension() :hRuler.getPreferredSize();
			Dimension vRulerPreferredSize = vRuler == null ? new Dimension() :vRuler.getPreferredSize();
			Dimension hbarPreferredSize = null;
			Dimension vbarPreferredSize = null;
			if(horScrollBar != null) {
				hbarPreferredSize = horScrollBar.getPreferredSize();
				vbarPreferredSize = verScrollBar.getPreferredSize();
			}
			if(hRuler != null) {
				hRuler.setBounds(left + vRulerPreferredSize.width -1, top, right - vRulerPreferredSize.width, hRulerPreferredSize.height);
			}
			if(vRuler != null) {
				vRuler.setBounds(left, top + vRulerPreferredSize.height -1, vRulerPreferredSize.width, bottom - hRulerPreferredSize.height);
			}
			if(horScrollBar != null) {
				horScrollBar.setBounds(left + vRulerPreferredSize.height, bottom - hbarPreferredSize.height, right - vRulerPreferredSize.height - BARSIZE, hbarPreferredSize.height);
				verScrollBar.setBounds(right - vRulerPreferredSize.width, top + vRulerPreferredSize.height, vbarPreferredSize.width, bottom - hRulerPreferredSize.height - BARSIZE);
			}
			
			designer.setBounds(
					left + vRulerPreferredSize.width, 
					top + vRulerPreferredSize.height, 
					right - (vbarPreferredSize == null ? 0 : vbarPreferredSize.width) - vRulerPreferredSize.width,
					bottom - (hbarPreferredSize == null ? 0 : hbarPreferredSize.height * 2));
		}
	}
}