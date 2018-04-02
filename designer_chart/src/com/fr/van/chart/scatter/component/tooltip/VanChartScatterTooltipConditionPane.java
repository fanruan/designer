package com.fr.van.chart.scatter.component.tooltip;

import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;

public class VanChartScatterTooltipConditionPane extends VanChartTooltipConditionPane {

    private static final long serialVersionUID = 7514028150764584873L;

    public VanChartScatterTooltipConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    @Override
    protected VanChartPlotTooltipPane createTooltipContentsPane() {
        return new VanChartScatterPlotTooltipNoCheckPane(getPlot(), null);
    }
}