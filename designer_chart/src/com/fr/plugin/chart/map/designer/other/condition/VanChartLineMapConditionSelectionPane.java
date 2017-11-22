package com.fr.plugin.chart.map.designer.other.condition;

import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.plugin.chart.map.VanChartMapDataPoint;

/**
 * Created by hufan on 2016/12/26.
 */
public class VanChartLineMapConditionSelectionPane extends ChartConditionPane {

    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_NAME,
                ChartConstants.SERIES_INDEX,
                VanChartMapDataPoint.START_AREA_NAME,
                VanChartMapDataPoint.END_AREA_NAME,
                ChartConstants.VALUE
        };
    }
}
