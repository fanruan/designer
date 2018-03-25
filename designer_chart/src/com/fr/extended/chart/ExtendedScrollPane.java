package com.fr.extended.chart;

import com.fr.plugin.chart.designer.AbstractVanChartScrollPane;

/**
 * Created by shine on 2018/3/24.
 */
public abstract class ExtendedScrollPane<T> extends AbstractVanChartScrollPane<T> {

    @Override
    public abstract void updateBean(T ob);
}
