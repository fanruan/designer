package com.fr.plugin.chart.designer.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.FRLogger;
import com.fr.stable.FCloneable;

/**
 * 新的obj是clone出来的，非new
 */
public class VanChartUIMenuNameableCreator extends UIMenuNameableCreator {
    public VanChartUIMenuNameableCreator(String name, FCloneable obj, Class<? extends BasicBeanPane> paneClazz) {
        super(name, obj, paneClazz);
    }
    /**
     * 覆盖的clone方法
     */
    public UIMenuNameableCreator clone() {
        Object cloneObj = null;
        try {
            cloneObj = ((FCloneable)obj).clone();
        } catch (CloneNotSupportedException e){
            FRLogger.getLogger().error("VanChartUIMenuNameableCreator CloneNotSupportedException");
        }
        return new UIMenuNameableCreator(name, cloneObj, this.paneClazz);

    }
}