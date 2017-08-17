package com.fr.plugin.chart.gauge;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.tooltip.TooltipContentPaneWithOutSeries;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.plugin.chart.type.GaugeStyle;


public class VanChartGaugePlotTooltipPane extends VanChartPlotTooltipPane {
    private static final long serialVersionUID = 6087381131907589372L;

    public VanChartGaugePlotTooltipPane(Plot plot, VanChartStylePane parent) {
        super(plot,parent);
    }

    @Override
    protected void initTooltipContentPane(Plot plot){
        GaugeStyle gaugeStyle = ((VanChartGaugePlot)plot).getGaugeStyle();
        switch (gaugeStyle){
            case POINTER:
                tooltipContentPane = new VanChartTooltipContentPane(parent, VanChartGaugePlotTooltipPane.this);
                break;
            case POINTER_SEMI:
                tooltipContentPane = new VanChartTooltipContentPane(parent, VanChartGaugePlotTooltipPane.this);
                break;
            default:
                tooltipContentPane = new TooltipContentPaneWithOutSeries(parent, VanChartGaugePlotTooltipPane.this);
                break;
        }
    }


}