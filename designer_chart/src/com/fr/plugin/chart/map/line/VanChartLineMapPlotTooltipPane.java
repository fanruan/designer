package com.fr.plugin.chart.map.line;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;

/**
 * Created by hufan on 2016/12/19.
 */
public class VanChartLineMapPlotTooltipPane extends VanChartPlotTooltipPane{
    public VanChartLineMapPlotTooltipPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected void initTooltipContentPane(Plot plot){
        tooltipContentPane = new VanChartLineMapTooltipContentPane(parent, VanChartLineMapPlotTooltipPane.this);
    }

    protected boolean hasTooltipSeriesType() {
        return false;
    }
}
