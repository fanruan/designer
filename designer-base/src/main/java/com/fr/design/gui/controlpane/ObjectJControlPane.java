package com.fr.design.gui.controlpane;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fr.design.beans.BasicBeanPane;

/**
 * 生成带参数的BasicBeanPane
 * 
 * @author zhou
 * @since 2012-4-5上午9:29:20
 */
public abstract class ObjectJControlPane extends JListControlPane {
	private Object object;

	public ObjectJControlPane() {
		this(null);
	}

	public ObjectJControlPane(Object object) {
		super();
		this.object = object;
	}

	@Override
	protected BasicBeanPane createPaneByCreators(NameableCreator creator) {
		try {
			if (object == null) {
				return super.createPaneByCreators(creator);
			} else if (object.getClass().isArray()) {
				return creator.getUpdatePane().getConstructor(object.getClass()).newInstance(object);
			} else {
				Constructor<? extends BasicBeanPane> constructor = getConstructor(creator.getUpdatePane(), object.getClass());
				return constructor == null ? super.createPaneByCreators(creator) : constructor.newInstance(object);
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 传进BasicBeanPane的构造函数的参数，可能是
	 * 
	 * @param clazz
	 * @param cls
	 * @return
	 */
	private Constructor<? extends BasicBeanPane> getConstructor(Class<? extends BasicBeanPane> clazz, Class<?> cls) {
		Constructor<? extends BasicBeanPane> constructor = null;
		try {
			constructor = clazz.getConstructor(cls);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		if (constructor != null) {
			return constructor;
		} else {
			if (cls.getName() == Object.class.getName()) {
				return null;
			}
			return getConstructor(clazz, cls.getSuperclass());
		}
	}

}