/**
 * 
 */
package com.fr.design.form.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;

/**
 * 布局管理器，主要为一些需要加标题的组件用，如报表块、图表块
 * 
 * @author jim
 * @date 2014-9-25
 */
public class FRTitleLayout implements FRLayoutManager, LayoutManager{
	
	public static final String TITLE = "Title";
	
	public static final String BODY = "Body";
	
	// 标题控件，默认为文本框
	private Component title;
	// 主体控件，有表单报表块、图表块
	private Component body;
	private int gap;
	
	/**
	 * 构造函数
	 */
	public FRTitleLayout() {
		this(0);
	}
	
	/**
	 * 标题和主体间隙gap的布局
	 * @param gap  间隙值
	 */
	public FRTitleLayout(int gap) {
		this.gap = gap;
	}
	
	 /**
	  * 返回
     * Returns the  gap between components.
     */
    public int getGap() {
    	return gap;
    }

    /**
     * 设置
     * Sets the gap between components.
     * @param the gap between components
     */
    public void setGap(int gap) {
    	this.gap = gap;
    }
	
	 /**
	 * 增加组件
	 * @param name 名字
	 * @param comp 组件
	 */
  	public void addLayoutComponent(String name, Component comp) {
	      synchronized (comp.getTreeLock()) {
				if (ComparatorUtils.equals(name, null)) {
				    name = BODY;
				}
				if (ComparatorUtils.equals(name, BODY)) {
					body = comp;
				} else if (ComparatorUtils.equals(name, TITLE)) {
				    title = comp;
				} 
	      }
	 }

	/**
	 * 移除组件
	 * @param comp 组件
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		 synchronized (comp.getTreeLock()) {
			 if (comp == title) {
				 title = null;
			 } 
		 }
	}

	/**
	 * 最优大小
	 * @param parent 父容器
	 * @return 默认大小
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(WLayout.MIN_WIDTH, WLayout.MIN_HEIGHT*2);
	}

	/**
	 * 最小大小
	 * @param parent 父容器
	 * @return 默认初始大小
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// 有标题时，最小高度为两个组件高度
		return new Dimension(WLayout.MIN_WIDTH, title==null? WLayout.MIN_HEIGHT : WLayout.MIN_HEIGHT+WTitleLayout.TITLE_HEIGHT);
	}

	/**
	 * 布局刷新
	 * @param target 容器
	 */
	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			int width = target.getWidth();
			int height = target.getHeight();
			int titleH = title==null ? 0 : WTitleLayout.TITLE_HEIGHT;
			for (int i=0; i< target.getComponentCount(); i++) {
				Component comp = target.getComponent(i);
				if (comp != null && comp == title) {
					comp.setBounds(0, 0, width, WTitleLayout.TITLE_HEIGHT);
				} else if (comp != null && comp == body) {
					int y = titleH+gap;
					comp.setBounds(0, y, width, height-y);
				}
			}
		}
	}
	
	 public Object getConstraints(Component comp) {
        if (comp == null){
            return null;
        }
		if (comp == title) {
		    return TITLE;
		} else if (comp == body) {
		    return BODY;
		} 
		return null;
	 }

	/**
	 * 是否重置大小
	 * @return 是
	 */
	@Override
	public boolean isResizable() {
		return false;
	}

}