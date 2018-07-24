package com.fr.van.chart.multilayer.other;

import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;

import com.fr.plugin.chart.multilayer.VanChartMultiPieDataPoint;

/**
 * Created by Fangjie on 2016/6/16.
 */
public class VanChartMultiPieConditionSelectionPane extends ChartConditionPane {
    public static final String LEVEL_NAME = com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Level_Name");
    public static final String LEVEL_ORDER = com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Level_Order");


    public String[] columns2Populate() {
        return new String[]{
                VanChartMultiPieDataPoint.LEVEL_ORDER,
                VanChartMultiPieDataPoint.LEVEL_NAME,
                ChartConstants.VALUE
        };
    }
}
