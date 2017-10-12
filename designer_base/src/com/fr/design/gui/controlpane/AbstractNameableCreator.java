package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.js.JavaScript;

import javax.swing.*;


public abstract class AbstractNameableCreator implements NameableCreator {
	protected String menuName;
	protected Icon menuIcon;
	protected Class clazzOfObject;
	protected Class clazzOfInitCase;
	protected Class<? extends BasicBeanPane> clazzOfEditor;
	
	public AbstractNameableCreator(String menuName, Class clazz, Class<? extends BasicBeanPane> clazzOfEditor) {
		this.menuName = menuName;
		this.clazzOfObject = clazz;
		this.clazzOfInitCase = clazz;
		this.clazzOfEditor = clazzOfEditor;
	}

	public AbstractNameableCreator(String menuName, String iconPath, Class clazz) {
		this.menuName = menuName;
		this.menuIcon = BaseUtils.readIcon(iconPath);
		this.clazzOfObject = clazz;
		this.clazzOfInitCase = clazz;
	}
	
	public AbstractNameableCreator(String menuName, String iconPath, Class clazz, Class<? extends BasicBeanPane> clazzOfEditor) {
		this.menuName = menuName;
		this.menuIcon = BaseUtils.readIcon(iconPath);
		this.clazzOfObject = clazz;
		this.clazzOfEditor = clazzOfEditor;
		this.clazzOfInitCase = clazz;
	}
	
	public AbstractNameableCreator(String menuName, String iconPath, Class clazz, Class clazz4Init, Class<? extends BasicBeanPane> clazzOfEditor) {
		this.menuName = menuName;
		this.menuIcon = BaseUtils.readIcon(iconPath);
		this.clazzOfObject = clazz;
		this.clazzOfEditor = clazzOfEditor;
		this.clazzOfInitCase = clazz;
		this.clazzOfInitCase = clazz4Init;
	}

	/**
	 * get menuName
	 * @return
	 */
	public String menuName() {			
		return this.menuName;
	}

	/**
	 * get menuIcon
	 * @return
	 */
	public Icon menuIcon() {
		return this.menuIcon;
	}

	@Override
	public Class<? extends JavaScript> getHyperlink() {
		return this.clazzOfObject;
	}

	/**
	 * get clazzOfEditor
	 * @return
	 */
	public Class<? extends BasicBeanPane> getUpdatePane() {
		return this.clazzOfEditor;
	}

	/**
	 *
	 * @param ob
	 * @return
	 */
	public Object acceptObject2Populate(Object ob) {
		if (ob instanceof NameObject) {
			ob = ((NameObject)ob).getObject();
		}
		if (clazzOfObject != null && clazzOfObject.isInstance(ob)) {
			doSthChanged4Icon(ob);
			return ob;
		}
		
		return null;
	}

	protected void doSthChanged4Icon(Object ob){
		
	}

	/**
	 *
	 * @return
	 */
	public String createTooltip(){
		return null;
	}

    public boolean isNeedParameterWhenPopulateJControlPane(){
        return false;
    }

	public boolean equals(Object obj) {
		return obj instanceof AbstractNameableCreator
				&& ComparatorUtils.equals(menuName, ((AbstractNameableCreator) obj).menuName)
				&& ComparatorUtils.equals(clazzOfObject, ((AbstractNameableCreator) obj).clazzOfObject);
	}
}