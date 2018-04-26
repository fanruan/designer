package com.fr.design.gui.itable;


/**
 * 预想的 只比UITableDataModel 差别一件事: setValue 不做toString 保持编辑component存在.
 * 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-6 下午03:32:22
 */
public class UITableComponentModel extends UITableDataModel {

	public UITableComponentModel(int columnSize) {
		super(columnSize);
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(rowIndex >= values.size() || columnIndex >= columnSize || aValue == null) {
			return;
		}
		values.get(rowIndex)[columnIndex] = aValue;
	}
}