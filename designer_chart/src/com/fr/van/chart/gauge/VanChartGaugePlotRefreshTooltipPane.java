package com.fr.van.chart.gauge;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.van.chart.designer.component.VanChartRefreshTooltipContentPane;
import com.fr.van.chart.designer.component.VanChartTooltipContentPane;
import com.fr.van.chart.designer.component.tooltip.RefreshTooltipContentPaneWithOutSeries;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotRefreshTooltipPane;

/**
 * Created by mengao on 2017/6/11.
 */
public class VanChartGaugePlotRefreshTooltipPane extends VanChartPlotRefreshTooltipPane {
    public VanChartGaugePlotRefreshTooltipPane(Plot plot) {
        super(plot);
    }


    @Override
    protected VanChartTooltipContentPane getTooltipContentPane(Plot plot){
        GaugeStyle gaugeStyle = ((VanChartGaugePlot)plot).getGaugeStyle();
        switch (gaugeStyle){
            case POINTER:
                return new VanChartRefreshTooltipContentPane(parent, VanChartGaugePlotRefreshTooltipPane.this);
            case POINTER_SEMI:
                return new VanChartRefreshTooltipContentPane(parent, VanChartGaugePlotRefreshTooltipPane.this);
            default:
                return new RefreshTooltipContentPaneWithOutSeries(parent, VanChartGaugePlotRefreshTooltipPane.this);
        }
    }
}
