package com.fr.plugin.chart.map.designer.other.condition;

import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.map.VanChartMapDataPoint;

/**
 * Created by Mitisky on 16/6/1.
 */
public class VanChartMapConditionSelectionPane extends ChartConditionPane {

    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_NAME,
                VanChartMapDataPoint.AREA_NAME,
                ChartConstants.VALUE
        };
    }
}
