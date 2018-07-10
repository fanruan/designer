package com.fr.van.chart.designer.style.background;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.log.FineLoggerFactory;
import com.fr.general.NameObject;
import com.fr.plugin.chart.attr.axis.VanChartAlertValue;
import com.fr.stable.Nameable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/8/21.
 */
public class ChartNameObjectCreator extends NameObjectCreator {
    protected Object object;


    public ChartNameObjectCreator(Object object, String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
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
        Constructor<? extends VanChartAlertValue> constructor = null;
        try {
            constructor = clazzOfInitCase.getConstructor();
            VanChartAlertValue vanChartAlertValue = constructor.newInstance();
            vanChartAlertValue.setAxisNamesArray((String[]) object);
            vanChartAlertValue.setAxisName(((String[]) object)[0]);
            return new NameObject(helper.createUnrepeatedName(this.menuName()), vanChartAlertValue);

        } catch (NoSuchMethodException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (InstantiationException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }
}
