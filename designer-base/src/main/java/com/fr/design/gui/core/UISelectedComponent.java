package com.fr.design.gui.core;

import javax.swing.event.ChangeListener;

/**
 * 这个接口说明一个基本组件是可以选择的
 * 
 * @author zhou
 * @since 2012-5-17下午4:38:17
 */
public interface UISelectedComponent {
	/**
	 * isSelected
	 * 
	 * @return
	 */
	public boolean isSelected();

	/**
	 * setSelected
	 * 
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected);
	
	/**
	 * the selected changed listener
	 * @param l
	 */
	void addChangeListener(ChangeListener l);

}