package com.fr.design.gui.controlpane;

import com.fr.general.NameObject;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.stable.Nameable;


public class NameObjectCreator extends AbstractNameableCreator {
	
	/*
	 * alex:这里的构造函数为什么传Class而不是传实例呢?
	 * 因为不应该NameObjectCreator初始化的时候就把BasicBeanPane初始化，而应该在需要用到这个面板的时候再做BasicBeanPane的初始化操作
	 */
	public NameObjectCreator(String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, clazz, updatePane);
	}

	public NameObjectCreator(String menuName, String iconPath, Class clazz) {
		super(menuName, iconPath, clazz);
	}
	
	public NameObjectCreator(String menuName, String iconPath, Class clazz, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, iconPath, clazz, updatePane);
	}
	
	public NameObjectCreator(String menuName, String iconPath, Class clazz, Class clazz4Init, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, iconPath, clazz, clazz4Init, updatePane);
	}

	/**
	 * create Nameable
	 * @param helper
	 * @return
	 */
	public Nameable createNameable(UnrepeatedNameHelper helper) {
		try {
			return new NameObject(helper.createUnrepeatedName(this.menuName()), clazzOfInitCase.newInstance());
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * save update bean
	 * @param wrapper
	 * @param bean
	 */
	public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
		((NameObject)wrapper.wrapper).setObject(bean);
	}
}