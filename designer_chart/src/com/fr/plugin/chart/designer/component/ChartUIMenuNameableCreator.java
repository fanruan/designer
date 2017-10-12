package com.fr.plugin.chart.designer.component;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.FRLogger;

import java.lang.reflect.Constructor;

/**
 * Created by hufan on 2016/11/15.
 */
public class ChartUIMenuNameableCreator extends UIMenuNameableCreator {
    private Plot plot;

    public ChartUIMenuNameableCreator(Plot plot, String name, Object obj, Class<? extends BasicBeanPane> paneClazz) {
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
        return new ChartUIMenuNameableCreator(plot, name, cloneObj, (Class<? extends BasicBeanPane>) this.paneClazz);

    }

    public BasicBeanPane getPane() {
        try {
            if (plot != null) {
                Constructor<? extends BasicBeanPane> constructor = paneClazz.getConstructor(Plot.class);
                return constructor.newInstance(plot);
            }
        } catch (Exception e) {
            return super.getPane();
        }
        return null;
    }
}
