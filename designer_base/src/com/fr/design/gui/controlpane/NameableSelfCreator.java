package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.stable.Nameable;


public abstract class NameableSelfCreator extends AbstractNameableCreator {
	
	public NameableSelfCreator(String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, clazz, updatePane);
	}
	
	public NameableSelfCreator(String menuName, String iconPath, Class clazz, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, iconPath, clazz, updatePane);
	}

	@Override
	public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
		wrapper.wrapper = (Nameable)bean;
	}

}