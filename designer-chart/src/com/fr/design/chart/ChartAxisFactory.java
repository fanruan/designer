package com.fr.design.chart;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.CategoryAxis;
import com.fr.chart.chartattr.RadarAxis;
import com.fr.chart.chartattr.ValueAxis;
import com.fr.design.mainframe.chart.gui.style.axis.*;
import com.fr.general.ComparatorUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : 上午11:19
 */
public class ChartAxisFactory {
    private static final String PERCENT = "Percent";
    private static final String SECOND = "Second";

    private static Map<String, Class<? extends ChartAxisUsePane>> map = new HashMap<String, Class<? extends ChartAxisUsePane>>();

    static {
        map.put(ValueAxis.class.getName(), ChartValuePane.class);
        map.put(RadarAxis.class.getName(), ChartRadarPane.class);
        map.put(CategoryAxis.class.getName(), ChartCategoryPane.class);
        map.put(ValueAxis.class.getName() + PERCENT, ChartPercentValuePane.class);
        map.put(ValueAxis.class.getName() + SECOND, ChartSecondValuePane.class);
    }
    private ChartAxisFactory() {

    }

    public static ChartAxisUsePane createAxisStylePane(Axis axis, String axisType) {
        String clsName = axis.getClass().getName();
        if (axis.isPercentage()) {
            clsName += PERCENT;
        } else if(ComparatorUtils.equals(axisType, "secondAxis")) {
        	clsName += SECOND;
        }
        Class<? extends ChartAxisUsePane> cls = map.get(clsName);
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return new ChartCategoryPane();
    }
}