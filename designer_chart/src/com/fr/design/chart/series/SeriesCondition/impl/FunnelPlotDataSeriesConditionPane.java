package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.FunnelPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.PiePlotChartConditionPane;

/**
 * Created by Mitisky on 16/11/9.
 */
public class FunnelPlotDataSeriesConditionPane extends DataSeriesConditionPane {

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new PiePlotChartConditionPane();
    }

    /**
     * 返回class对象
     * @return class对象
     */
    @Override
    public Class<? extends Plot> class4Correspond() {
        return FunnelPlot.class;
    }

}
