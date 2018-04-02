package com.fr.van.chart.multilayer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;

/**
 * Created by shine on 2017/3/13.
 */
public class VanChartMultiPieTooltipConditionPane extends VanChartTooltipConditionPane {
    public VanChartMultiPieTooltipConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    @Override
    protected VanChartPlotTooltipPane createTooltipContentsPane() {
        return new VanChartMultiPiePlotTooltipNoCheckPane(getPlot(), null);
    }
}
