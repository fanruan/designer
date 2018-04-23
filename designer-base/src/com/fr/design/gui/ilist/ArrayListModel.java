package com.fr.design.gui.ilist;

import javax.swing.AbstractListModel;

public class ArrayListModel extends AbstractListModel {
	private Object[] array;
	
	public ArrayListModel() {
		
	}
	
	public ArrayListModel(Object[] array) {
		this.array = array;
	}
	
	public Object getElementAt(int index) {
		return array != null ? array[index] : null;
	}

	public int getSize() {
		return array == null ? 0 : array.length;
	}

}