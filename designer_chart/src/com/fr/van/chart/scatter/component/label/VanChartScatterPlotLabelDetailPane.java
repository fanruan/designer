package com.fr.van.chart.scatter.component.label;

import com.fr.chart.chartattr.Plot;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.label.VanChartPlotLabelDetailPane;
import com.fr.van.chart.scatter.VanChartScatterLabelContentPane;



/**
 * 散点图的分类，多x、y标签
 */
public class VanChartScatterPlotLabelDetailPane extends VanChartPlotLabelDetailPane {

    private static final long serialVersionUID = 5176535960949074940L;
    public VanChartScatterPlotLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected void initToolTipContentPane(Plot plot) {
        dataLabelContentPane = new VanChartScatterLabelContentPane(parent,VanChartScatterPlotLabelDetailPane.this);
    }
}