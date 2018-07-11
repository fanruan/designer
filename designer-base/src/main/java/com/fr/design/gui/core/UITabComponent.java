package com.fr.design.gui.core;

import javax.swing.event.ChangeListener;

/**
 * 这个接口说明一个基本组件是类似于tabbedpane的形式
 * 
 * @author zhou
 * @since 2012-5-17下午4:46:00
 */
public interface UITabComponent {
	/**
	 * 获取当前选中的tab
	 * 
	 * @return
	 */
	public int getSelectedIndex();

	/**
	 * 设置选中的tab
	 * 
	 * @param newSelectedIndex
	 */
	public void setSelectedIndex(int newSelectedIndex);
	
	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addChangeListener(ChangeListener l);
	
	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeChangeListener(ChangeListener l);
}