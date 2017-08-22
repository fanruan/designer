package com.fr.plugin.chart.designer.other;

import com.fr.base.chart.BasePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.plugin.chart.designer.component.ConditionUIMenuNameableCreator;
import com.fr.stable.Nameable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/8/18.
 */
public class ChartConditionNameObjectCreator extends NameObjectCreator {
    private BasePlot plot;
    private ConditionUIMenuNameableCreator conditionUIMenuNameableCreator;

    public ChartConditionNameObjectCreator(BasePlot plot, String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
        super(menuName, clazz, updatePane);
        this.plot = plot;
    }

    /**
     * create Nameable
     *
     * @param helper
     * @return
     */
    public Nameable createNameable(UnrepeatedNameHelper helper) {
        Constructor<? extends ConditionUIMenuNameableCreator> constructor = null;
        try {
            constructor = clazzOfInitCase.getConstructor(Plot.class, String.class, Object.class, Class.class);
            ConditionUIMenuNameableCreator conditionUIMenuNameableCreator = constructor.newInstance(plot, Inter.getLocText("Chart-Condition_Attributes"), new ConditionAttr(), getUpdatePane());
            return new NameObject(helper.createUnrepeatedName(this.menuName()), conditionUIMenuNameableCreator);

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
            conditionUIMenuNameableCreator = (ConditionUIMenuNameableCreator) ((ConditionUIMenuNameableCreator) ob).clone();
            return ob;
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
        conditionUIMenuNameableCreator.setObj(bean);
        ((NameObject) wrapper.wrapper).setObject(conditionUIMenuNameableCreator);
    }


}
