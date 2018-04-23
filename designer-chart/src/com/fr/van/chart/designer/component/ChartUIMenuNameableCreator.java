package com.fr.van.chart.designer.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.FRLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by hufan on 2016/11/15.
 */
public class ChartUIMenuNameableCreator extends UIMenuNameableCreator {
    private HashMap hyperLinkEditorMap;

    public ChartUIMenuNameableCreator(HashMap hyperLinkEditorMap, String name, Object obj, Class<? extends BasicBeanPane> paneClazz) {
        super(name, obj, paneClazz);
        this.hyperLinkEditorMap = hyperLinkEditorMap;
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
        return new ChartUIMenuNameableCreator(hyperLinkEditorMap, name, cloneObj, (Class<? extends BasicBeanPane>) this.paneClazz);

    }

    public BasicBeanPane getPane() {
        try {
            if (hyperLinkEditorMap != null) {
                Constructor<? extends BasicBeanPane> constructor = paneClazz.getConstructor(HashMap.class, boolean.class);
                return constructor.newInstance(hyperLinkEditorMap, true);
            }
        } catch (Exception e) {
            return super.getPane();
        }
        return null;
    }
}
