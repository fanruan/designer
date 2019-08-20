package com.fr.design.gui.controlpane;

import java.lang.reflect.Constructor;
import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.AssistUtils;

/**
 * Created by plough on 2017/8/1.
 */
public abstract class ObjectUIControlPane extends UIListControlPane {
    private Object object;

    public ObjectUIControlPane(Object object) {
        super();
        this.object = object;
    }

    @Override
    public BasicBeanPane createPaneByCreators(NameableCreator creator) {
        try {
            if (object == null) {
                return super.createPaneByCreators(creator);
            } else if (object.getClass().isArray()) {
                return creator.getUpdatePane().getConstructor(object.getClass()).newInstance(object);
            } else {
                Constructor<? extends BasicBeanPane> constructor = getConstructor(creator.getUpdatePane(), object.getClass());
                return constructor == null ? super.createPaneByCreators(creator) : constructor.newInstance(object);
            }
        } catch (Exception e) {
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
            if (AssistUtils.equals(cls.getName(),Object.class.getName())) {
                return null;
            }
            return getConstructor(clazz, cls.getSuperclass());
        }
    }

}