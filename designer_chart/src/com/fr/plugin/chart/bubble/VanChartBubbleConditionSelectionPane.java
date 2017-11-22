package com.fr.plugin.chart.bubble;

import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.scatter.VanChartScatterDataPoint;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleConditionSelectionPane extends ChartConditionPane {


    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_INDEX,
                ChartConstants.SERIES_NAME,
                VanChartScatterDataPoint.X,
                VanChartScatterDataPoint.Y,
                ChartConstants.VALUE
        };
    }
}
