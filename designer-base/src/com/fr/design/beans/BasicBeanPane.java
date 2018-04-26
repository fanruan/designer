package com.fr.design.beans;

import com.fr.design.dialog.BasicPane;

/**
 * Update Pane
 */
public abstract class BasicBeanPane<T> extends BasicPane {
    


	/**
	 * Populate.
	 */
	public abstract void populateBean(T ob);

	/**
	 * Update.
	 */
	public abstract T updateBean();

	public void updateBean(T ob) {

	}

	/**
	 * 更新权限工具栏面板
	 */
	public void populateAuthority() {

	}

	/**
	 * 仅用来处理图表设计器的地图面板
	 * @param mapType 地图类型
	 */
	public void dealWidthMap(String mapType){

	}

}