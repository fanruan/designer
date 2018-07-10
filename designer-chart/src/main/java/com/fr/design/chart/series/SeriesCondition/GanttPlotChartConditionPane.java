package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 下午3:13
 */
public class GanttPlotChartConditionPane extends ChartConditionPane {

    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.PROJECT_ID,
                ChartConstants.STEP_INDEX,
                ChartConstants.STEP_NAME
        };
    }
}