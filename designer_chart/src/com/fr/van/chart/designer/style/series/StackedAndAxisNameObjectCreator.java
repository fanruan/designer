package com.fr.van.chart.designer.style.series;

import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.general.FRLogger;
import com.fr.general.NameObject;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.stable.Nameable;
import com.fr.van.chart.designer.style.background.ChartNameObjectCreator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/9/11.
 */
public class StackedAndAxisNameObjectCreator extends ChartNameObjectCreator {

    public StackedAndAxisNameObjectCreator(Object object, String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
        super(object, menuName, clazz, updatePane);
    }

    /**
     * create Nameable
     *
     * @param helper
     * @return
     */
    public Nameable createNameable(UnrepeatedNameHelper helper) {
        Constructor<? extends ConditionAttr> constructor = null;
        try {
            constructor = clazzOfInitCase.getConstructor();
            ConditionAttr conditionAttr = constructor.newInstance();
            conditionAttr.addDataSeriesCondition((AttrSeriesStackAndAxis) object);
            return new NameObject(helper.createUnrepeatedName(this.menuName()), conditionAttr);

        } catch (NoSuchMethodException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } catch (InstantiationException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

}
