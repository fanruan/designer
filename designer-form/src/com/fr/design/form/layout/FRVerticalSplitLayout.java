package com.fr.design.form.layout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.fr.design.gui.itextfield.UITextField;

/**
 * @author richer
 * @since 6.5.4 垂直方向上的均分布局，上下组件高度比为ratio : (1 - ratio)
 */
public class FRVerticalSplitLayout extends FRSplitLayout {
	public FRVerticalSplitLayout() {
		super();
	}

	public FRVerticalSplitLayout(double ratio) {
		super(ratio);
	}

	public FRVerticalSplitLayout(double ratio, int hgap, int vgap) {
		super(ratio, hgap, vgap);
	}

	@Override
	protected Dimension calculateAppropriateSize(Container target, Dimension asideDim, Dimension centerDim) {
		Dimension dim = new Dimension(0, 0);
		Insets insets = target.getInsets();
		dim.width += insets.left + insets.right + hgap * 2;
		dim.height += insets.top + insets.bottom + vgap;
		int totalHeight = 0;
		if (asideDim != null) {
			totalHeight = (int) (asideDim.getHeight() / ratio);
		}
		if (centerDim != null) {
			int totalHeightFromCenter = (int) (centerDim.getHeight() / (1 - ratio));
			if (totalHeightFromCenter > totalHeight) {
				totalHeight = totalHeightFromCenter;
			}
		}
		dim.height += totalHeight;
		return dim;
	}

	@Override
	public void layoutContainer(Container target) {
		Insets insets = target.getInsets();
		// 这个布局只有两个组件
		int availableWidth = target.getWidth() - insets.left - insets.right - hgap * 2;
		int availableHeight = target.getHeight() - insets.top - insets.bottom - vgap;
		int asideHeight = 0;
		if (aside != null) {
			asideHeight = (int) (availableHeight * ratio);
			aside.setBounds(insets.left + hgap, insets.top + vgap, availableWidth, asideHeight);
		}
		if (center != null) {
			center.setBounds(insets.left + hgap, insets.top + asideHeight + vgap, availableWidth, availableHeight - asideHeight);
		}
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("垂直均分布局测试");
		JPanel p = (JPanel) f.getContentPane();
		p.setLayout(new FRVerticalSplitLayout(0.2, 2, 2));
		UITextField f1 = new UITextField("上边");
		p.add(f1, FRSplitLayout.ASIDE);
		UITextField f2 = new UITextField("下边");
		p.add(f2, FRSplitLayout.CENTER);
		f.setSize(300, 210);
		f.setVisible(true);
		System.out.println(f1.getSize());
		System.out.println(f2.getSize());
	}

}