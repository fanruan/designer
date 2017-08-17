package com.fr.plugin.chart.designer.component;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.FRLogger;

import java.lang.reflect.Constructor;

/**
 * Created by hufan on 2016/11/15.
 */
public class ConditionUIMenuNameableCreator extends UIMenuNameableCreator {
    private Plot plot;

    public ConditionUIMenuNameableCreator(Plot plot, String name, Object obj, Class<? extends ConditionAttributesPane> paneClazz) {
        super(name, obj, paneClazz);
        this.plot = plot;
    }

    public UIMenuNameableCreator clone() {
        Object cloneObj = null;
        try {
            cloneObj = obj.getClass().newInstance();
        } catch (InstantiationException e) {
            FRLogger.getLogger().error("UIMenuNameableCreator InstantiationException");
        } catch (IllegalAccessException e) {
            FRLogger.getLogger().error("UIMenuNameableCreator IllegalAccessException");
        }
        return new ConditionUIMenuNameableCreator(plot, name, cloneObj, (Class<? extends ConditionAttributesPane>) this.paneClazz);

    }

    public BasicBeanPane getPane() {
        try {
            Constructor<? extends BasicBeanPane> constructor = paneClazz.getConstructor(Plot.class);
            return constructor.newInstance(plot);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return null;
    }
}
