package com.fr.plugin.chart.scatter;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.ConfigHelper;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;


public class VanChartScatterPlotTooltipPane extends VanChartPlotTooltipPane {
    private static final long serialVersionUID = 6087381131907589371L;
    public VanChartScatterPlotTooltipPane(Plot plot, VanChartStylePane parent) {
        super(plot,parent);
    }

    @Override
    protected void initTooltipContentPane(Plot plot){
        tooltipContentPane = new VanChartScatterTooltipContentPane(parent, VanChartScatterPlotTooltipPane.this);
    }

    @Override
    protected AttrTooltip getAttrTooltip() {
        //設置默認值
        return ConfigHelper.getDefaultScatterAttrTooltip();
    }

}