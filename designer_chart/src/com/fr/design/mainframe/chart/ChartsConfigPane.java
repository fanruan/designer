package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Charts;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

import javax.swing.*;

/**
 * Created by mengao on 2017/5/16.
 */
public abstract class ChartsConfigPane <T extends Charts> extends AbstractChartAttrPane {

    public final static String CHART_STYLE_TITLE = Inter.getLocText("Chart-Style_Name");

    public abstract Class<? extends Charts> acceptType();

    @Override
    public void populate(ChartCollection collection) {
        if (StableUtils.classInstanceOf(collection.getSelectedChart().getClass(), acceptType())) {
            populate(collection, (T)collection.getSelectedChart());
        }
    }

    protected abstract void populate(ChartCollection collection, T selectedChart);

    @Override
    public void update(ChartCollection collection) {
        if (StableUtils.classInstanceOf(collection.getSelectedChart().getClass(), acceptType())) {
            update(collection, (T)collection.getSelectedChart());
        }
    }

    protected abstract void update(ChartCollection collection, T selectedChart);

    @Override
    protected JPanel createContentPane() {
        return new JPanel();
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/chart/ChartStyle.png";
    }

    @Override
    public String title4PopupWindow() {
        return CHART_STYLE_TITLE;
    }
}
