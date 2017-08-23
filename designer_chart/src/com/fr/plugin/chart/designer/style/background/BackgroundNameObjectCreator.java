package com.fr.plugin.chart.designer.style.background;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.general.NameObject;
import com.fr.plugin.chart.base.VanChartCustomIntervalBackground;
import com.fr.stable.Nameable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/8/23.
 */
public class BackgroundNameObjectCreator extends ChartNameObjectCreator {
    public BackgroundNameObjectCreator(Object object, String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
        super(object, menuName, clazz, updatePane);
    }

    /**
     * create Nameable
     *
     * @param helper
     * @return
     */
    public Nameable createNameable(UnrepeatedNameHelper helper) {
        Constructor<? extends VanChartCustomIntervalBackground> constructor = null;
        try {
            constructor = clazzOfInitCase.getConstructor();
            VanChartCustomIntervalBackground vanChartCustomIntervalBackground = constructor.newInstance();
            vanChartCustomIntervalBackground.setAxisNamesArray((String[]) object);
            vanChartCustomIntervalBackground.setAxisName(((String[]) object)[0]);
            return new NameObject(helper.createUnrepeatedName(this.menuName()), vanChartCustomIntervalBackground);

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

}
