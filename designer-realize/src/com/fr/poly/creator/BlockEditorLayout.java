/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.ibutton.UIButton;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-6
 */
public class BlockEditorLayout implements LayoutManager, java.io.Serializable {
	public static final String CENTER = "center";
	public static final String LEFTBOTTOM = "leftbottom";
	public static final String RIGHTTOP = "righttop";
	public static final String BOTTOMCORNER = "bottomcorner";
	public static final String TOP = "top";

	private Component center;
	private Component lb;
	private Component rt;
	private Component bc;
	private Component tp;

	public BlockEditorLayout() {

	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (CENTER.equals(name)) {
			this.center = comp;
		} else if (LEFTBOTTOM.equals(name)) {
			this.lb = comp;
		} else if (RIGHTTOP.equals(name)) {
			this.rt = comp;
		} else if (BOTTOMCORNER.equals(name)) {
			this.bc = comp;
		} else if (TOP.equals(name)) {
			this.tp = comp;
		}
	}

	@Override
	public void removeLayoutComponent(Component comp) {

	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			Dimension d = new Dimension();
			Dimension cps = center.getPreferredSize();
			Dimension lps = lb.getPreferredSize();
			Dimension rps = rt.getPreferredSize();
			Dimension tps = tp.getPreferredSize();
			d.height = insets.top + insets.bottom + cps.height + lps.height + tps.height;
			d.width = insets.left + insets.right + cps.width + rps.width;
			return d;
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			Dimension d = new Dimension();
			Dimension cps = center.getMinimumSize();
			Dimension lps = lb.getMinimumSize();
			Dimension rps = rt.getMinimumSize();
			Dimension tps = tp.getMinimumSize();
			d.height = insets.top + insets.bottom + cps.height + lps.height + tps.height;
			d.width = insets.left + insets.right + cps.width + rps.width;
			return d;
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
			Dimension lb_d = lb.getPreferredSize();
			Dimension rt_d = rt.getPreferredSize();

			if (tp == null) {
				lb.setBounds(left, bottom - lb_d.height, lb_d.width, lb_d.height);
				rt.setBounds(right - rt_d.width, top, rt_d.width, rt_d.height);
				center.setBounds(left, top, right - rt_d.width, bottom - lb_d.height);

			} else {
				Dimension tp_d = tp.getPreferredSize();
				tp.setBounds(left, top, tp_d.width, tp_d.height);
				center.setBounds(left, top + tp_d.height, right - rt_d.width, bottom - lb_d.height - tp_d.height);
				lb.setBounds(left, bottom - lb_d.height, lb_d.width, lb_d.height);
				rt.setBounds(right - rt_d.width, top + tp_d.height, rt_d.width, rt_d.height);
			}
			if (bc != null) {
				bc.setBounds(right - rt_d.width * 2, bottom - lb_d.height * 2, rt_d.width * 2, lb_d.height * 2);
			}
		}
	}

	public static void main(String... args) {
		JFrame f = new JFrame();
		JPanel p = (JPanel) f.getContentPane();
		p.setLayout(new BlockEditorLayout());
		UILabel center = new UILabel("111");
		center.setBorder(BorderFactory.createLineBorder(Color.red));
		p.add(CENTER, center);
		UIButton btn1 = new UIButton();
		btn1.setPreferredSize(new Dimension(30, 15));
		p.add(LEFTBOTTOM, btn1);
		UIButton btn2 = new UIButton();
		btn2.setPreferredSize(new Dimension(15, 30));
		p.add(RIGHTTOP, btn2);
		UIButton btn3 = new UIButton();
		p.add(BOTTOMCORNER, btn3);
		UIButton btn4 = new UIButton();
		p.add(TOP, btn4);
		f.setSize(300, 200);
		f.setVisible(true);
	}
}