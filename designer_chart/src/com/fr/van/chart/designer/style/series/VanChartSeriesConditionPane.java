package com.fr.van.chart.designer.style.series;

import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;

/**
 * 只对系列进行设置
 */
public class VanChartSeriesConditionPane extends ChartConditionPane {

    /**
     * 只对系列进行设置
     * @return 系列值，系列名
     */
    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_INDEX,
                ChartConstants.SERIES_NAME
        };
    }
}