package com.fr.design.gui.core;

/**
 * 带render功能
 * 
 * @author zhou
 * @since 2012-5-28下午4:27:24
 */
public interface UIFurtherTabComponent<T> extends UITabComponent {

	/**
	 * set the SelectedItem by the element
	 * 
	 * @param ob
	 */
	public void setSelectedItem(T element);

	/**
	 * get the SelectedItem
	 * 
	 * @return
	 */
	public T getSelectedItem();
}