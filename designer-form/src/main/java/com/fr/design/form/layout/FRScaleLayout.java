/**
 * 
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;

/**
 * @author jim
 * @date 2014-9-23
 */
public class FRScaleLayout implements FRLayoutManager{

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
		return new Dimension(WLayout.MIN_WIDTH, WFitLayout.MIN_HEIGHT);
	}

	/**
	 * 最小大小
	 * @param parent 父容器
	 * @return 默认初始大小
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(WLayout.MIN_WIDTH, WFitLayout.MIN_HEIGHT);
	}

	/**
	 * 布局刷新
	 * @param target 容器
	 */
	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			if (parent.getComponentCount() < 1) {
				return;
			}
			// 考虑布局用内边距，所以文本框都控件特殊用的当前layout刷新下子控件
			Component comp = parent.getComponent(0);
			if (comp != null) {
				Rectangle rec = parent.getBounds();
				comp.setBounds(0, 0, rec.width, comp.getHeight());
			}
		}
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