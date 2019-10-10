package com.fr.design.chartx.impl;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.impl.AbstractChartWithData;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;

/**
 * Created by shine on 2019/09/04.
 */
public abstract class AbstractOtherPane<T extends AbstractChartWithData> extends AbstractChartAttrPane {

    protected abstract void populate(T chart);

    protected abstract void update(T chart);

    @Override
    public void populate(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        AbstractChartWithData chart = collection.getSelectedChart(AbstractChartWithData.class);
        if (chart == null) {
            return;
        }

        populate((T) chart);
    }

    @Override
    public void update(ChartCollection collection) {

        if (collection == null) {
            return;
        }
        AbstractChartWithData chart = collection.getSelectedChart(AbstractChartWithData.class);
        if (chart == null) {
            return;
        }

        update((T) chart);
    }
}
