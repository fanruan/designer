package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;

import com.fr.form.ui.container.WLayout;

public class FRFitLayout implements FRLayoutManager, LayoutManager{
	
	private int interval;
	// 上次的布局内边距
	private Insets lastInset;

	public FRFitLayout() {
		this(0);
	}
	
	public FRFitLayout(int interval) {
		this.interval = interval;
	}
	
	public void setInterVal(int val) {
		this.interval = val;
	}

	/**
	 * 增加组件
	 * @param name 名字
	 * @param comp 组件
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		
	}

	/**
	 * 移除组件
	 * @param comp 组件
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		
	}

	/**
	 * 最优大小
	 * @param parent 父容器
	 * @return 默认大小
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(WLayout.DEFAULT_WIDTH, WLayout.DEFAULT_HEIGHT);
	}

	/**
	 * 最小大小
	 * @param parent 父容器
	 * @return 默认初始大小
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(WLayout.DEFAULT_WIDTH, WLayout.MIN_HEIGHT);
	}

	/**
	 * 布局刷新
	 * @param target 容器
	 */
	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int width = target.getWidth();
			int height = target.getHeight();
			calculateLastInset(target, width, height);
			if (insetNotChange(insets, lastInset)) {
				return;
			}
			// 设置内边距时已经判断过，此处直接加上内边距
			for (int i=0, len=target.getComponentCount(); i<len; i++ ) {
				Component comp = target.getComponent(i);
				Rectangle rec = comp.getBounds();
				Rectangle bound = new Rectangle(rec);
				if (rec.x == lastInset.left) {
					bound.x += (insets.left - lastInset.left);
					bound.width -= (insets.left - lastInset.left);
				}
				if (rec.y == lastInset.top) {
					bound.y += (insets.top - lastInset.top);
					bound.height -= (insets.top - lastInset.top);
				}
				if (rec.x+rec.width == width-lastInset.right) {
					bound.width -= (insets.right - lastInset.right);
				}
				if (rec.y+rec.height == height-lastInset.bottom) {
					bound.height -= (insets.bottom - lastInset.bottom);
				}
				comp.setBounds(bound);
			}
		}
	}
	
	private void calculateLastInset(Container target, int width, int height) {
		int len = target.getComponentCount();
		if (len ==0) {
			lastInset = new Insets(0, 0, 0, 0);
			return;
		}
		Point initRec = target.getComponent(0).getLocation();
		lastInset = new Insets(initRec.y, initRec.x, target.getHeight(), target.getWidth());
		for (int i=0; i<len; i++) {
			Component co = target.getComponent(i);
			Rectangle rec = co.getBounds();
			lastInset.left = Math.min(rec.x, lastInset.left);
			lastInset.right = Math.min(width-rec.x-rec.width, lastInset.right);
			lastInset.top = Math.min(rec.y, lastInset.top);
			lastInset.bottom = Math.min(height-rec.y-rec.height, lastInset.bottom);
		}
	}
	
	private boolean insetNotChange(Insets inset, Insets last) {
		return inset.left == last.left && inset.right == last.right && inset.top == last.top && inset.bottom == last.bottom;
	}
	
	/**
	 * 是否重置大小
	 * @return 是
	 */
	@Override
	public boolean isResizable() {
		return true;
	}

}