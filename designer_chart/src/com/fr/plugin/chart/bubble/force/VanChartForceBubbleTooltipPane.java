package com.fr.plugin.chart.bubble.force;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartTooltipPane;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartForceBubbleTooltipPane extends VanChartTooltipPane {
    public VanChartForceBubbleTooltipPane(VanChartStylePane parent) {
        super(parent);
    }
    protected VanChartPlotTooltipPane getTooltipPane(Plot plot) {
        return new VanChartPlotTooltipPane(plot, parent);
    }
}
