package com.fr.plugin.chart.bubble.force;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.designer.component.VanChartRefreshTooltipContentPane;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotRefreshTooltipPane;
import com.fr.plugin.chart.scatter.VanChartScatterRefreshTooltipContentPane;

/**
 * Created by mengao on 2017/6/12.
 */
public class VanChartBubbleRefreshTooltipPane extends VanChartPlotRefreshTooltipPane {

    public VanChartBubbleRefreshTooltipPane(Plot plot) {
        super(plot);
    }

    @Override
    protected VanChartTooltipContentPane getTooltipContentPane(Plot plot){
        if (((VanChartBubblePlot)plot).isForceBubble()) {
             return new VanChartRefreshTooltipContentPane(parent, VanChartBubbleRefreshTooltipPane.this);
        } else {
            return new VanChartScatterRefreshTooltipContentPane(parent, VanChartBubbleRefreshTooltipPane.this);
        }

    }
}
