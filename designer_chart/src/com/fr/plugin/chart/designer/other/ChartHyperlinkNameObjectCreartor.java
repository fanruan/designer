package com.fr.plugin.chart.designer.other;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/8/21.
 */
public class ChartHyperlinkNameObjectCreartor extends NameObjectCreator {
    private Object object;
    private UIMenuNameableCreator uIMenuNameableCreator;


    public ChartHyperlinkNameObjectCreartor(Object object, String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
        super(menuName, clazz, updatePane);
        this.object = object;

    }

    /**
     * create Nameable
     *
     * @param helper
     * @return
     */
    public Nameable createNameable(UnrepeatedNameHelper helper) {
        Constructor<? extends UIMenuNameableCreator> constructor = null;
        try {
            constructor = clazzOfInitCase.getConstructor(String.class, Object.class, Class.class);
            UIMenuNameableCreator uIMenuNameableCreator = constructor.newInstance(menuName, object, getUpdatePane());
            return new NameObject(helper.createUnrepeatedName(this.menuName()), uIMenuNameableCreator);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param ob
     * @return
     */
    public Object acceptObject2Populate(Object ob) {
        if (ob instanceof NameObject) {
            ob = ((NameObject) ob).getObject();
        }
        if (clazzOfObject != null && clazzOfObject.isInstance(ob)) {
            doSthChanged4Icon(ob);
            uIMenuNameableCreator = ((UIMenuNameableCreator) ob).clone();
            if (uIMenuNameableCreator.getObj() != null && object.getClass().isInstance(uIMenuNameableCreator.getObj())) {
                return ob;
            }
        }

        return null;
    }

    /**
     * save update bean
     *
     * @param wrapper
     * @param bean
     */
    public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
        uIMenuNameableCreator.setObj(bean);
        ((NameObject) wrapper.wrapper).setObject(uIMenuNameableCreator);
    }


}
