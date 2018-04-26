package com.fr.design.chart;

import com.fr.design.chart.axis.ChartStyleAxisPane;
import com.fr.design.chart.series.SeriesCondition.dlp.DataLabelPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-5
 * Time   : 下午5:29
 */
public class FactoryObject {
    private Class<? extends ChartStyleAxisPane> axisPaneClass;
    private Class<? extends DataLabelPane> dataLabelPaneClass;

    public FactoryObject() {

    }

    public Class<? extends ChartStyleAxisPane> getAxisPaneClass() {
        return axisPaneClass;
    }

    public FactoryObject setAxisPaneCls(Class<? extends ChartStyleAxisPane> axisPaneClass) {
        this.axisPaneClass = axisPaneClass;
        return this;
    }

    public Class<? extends DataLabelPane> getDataLabelPaneClass() {
        return dataLabelPaneClass;
    }

    public FactoryObject setDataLabelPaneClass(Class<? extends DataLabelPane> dataLabelPaneClass) {
        this.dataLabelPaneClass = dataLabelPaneClass;
        return this;
    }

    public static FactoryObject EMPTY = new FactoryObject();
}