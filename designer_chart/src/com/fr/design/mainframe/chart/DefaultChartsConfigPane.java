package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Charts;

/**
 * Created by mengao on 2017/6/4.
 */
public class DefaultChartsConfigPane extends ChartsConfigPane {
    @Override
    public Class<? extends Charts> accptType() {
        return null;
    }

    @Override
    protected void populate(ChartCollection collection, Charts selectedChart) {

    }

    @Override
    protected void update(ChartCollection collection, Charts selectedChart) {

    }
}
