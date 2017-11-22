package com.fr.design.gui.imenutable;

import com.fr.design.beans.BasicBeanPane;
import com.fr.general.FRLogger;
import com.fr.stable.Nameable;

public class UIMenuNameableCreator implements Nameable{
	
	protected String name;
	protected Object obj;
	protected Class<? extends BasicBeanPane> paneClazz;
	
	public UIMenuNameableCreator(String name, Object obj, Class<? extends BasicBeanPane> paneClazz) {
		this.name = name;
		this.obj = obj;
		this.paneClazz = paneClazz;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Class<? extends BasicBeanPane> getPaneClazz() {
		return paneClazz;
	}

	/**
	 * 覆盖的clone方法
	 */
	public UIMenuNameableCreator clone() {
		Object cloneObj = null;
		try {
			cloneObj = obj.getClass().newInstance();
		} catch (InstantiationException e) {
			FRLogger.getLogger().error("UIMenuNameableCreator InstantiationException");
		} catch (IllegalAccessException e) {
			FRLogger.getLogger().error("UIMenuNameableCreator IllegalAccessException");
		}
		return new UIMenuNameableCreator(name, cloneObj, this.paneClazz);
		
	}
	
	public BasicBeanPane getPane() {
		try {
			return this.paneClazz.newInstance();
		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
		return null;
	}


}