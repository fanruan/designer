package com.fr.design.form.layout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.fr.design.gui.itextfield.UITextField;

/**
 * @author richer
 * @since 6.5.4 水平方向上的均分布局，左右组件宽度比为ratio : (1 - ratio)
 */
public class FRHorizontalSplitLayout extends FRSplitLayout {
	public FRHorizontalSplitLayout() {
		super();
	}

	public FRHorizontalSplitLayout(double ratio) {
		super(ratio);
	}

	public FRHorizontalSplitLayout(double ratio, int hgap, int vgap) {
		super(ratio, hgap, vgap);
	}

	@Override
	protected Dimension calculateAppropriateSize(Container target, Dimension asideDim, Dimension centerDim) {
		Dimension dim = new Dimension(0, 0);
		Insets insets = target.getInsets();
		dim.width += insets.left + insets.right + hgap;
		dim.height += insets.top + insets.bottom + vgap * 2;
		int totalWidth = 0;
		if (asideDim != null) {
			totalWidth = (int) (asideDim.getWidth() / ratio);
		}
		if (centerDim != null) {
			int totalWidthFromCenter = (int) (centerDim.getWidth() / (1 - ratio));
			if (totalWidthFromCenter > totalWidth) {
				totalWidth = totalWidthFromCenter;
			}
		}
		dim.width += totalWidth;
		return dim;
	}

	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			// 这个布局只有两个组件
			int availableWidth = target.getWidth() - insets.left - insets.right - hgap;
			int availableHeight = target.getHeight() - insets.top - insets.bottom - 2 * vgap;
			int asideWidth = 0;
			if (aside != null) {
				asideWidth = (int) (availableWidth * ratio);
				aside.setBounds(insets.left, insets.top + vgap, asideWidth, availableHeight);
			}
			if (center != null) {
				center.setBounds(insets.left + asideWidth + hgap, insets.top + vgap, availableWidth - asideWidth, availableHeight);
			}
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("水平均分布局测试");
		JPanel p = (JPanel) f.getContentPane();
		p.setLayout(new FRHorizontalSplitLayout(0.8, 2, 2));
		UITextField f1 = new UITextField("左边");
		p.add(f1, FRSplitLayout.ASIDE);
		UITextField f2 = new UITextField("右边");
		p.add(f2, FRSplitLayout.CENTER);
		f.setSize(300, 200);
		f.setVisible(true);
	}
}