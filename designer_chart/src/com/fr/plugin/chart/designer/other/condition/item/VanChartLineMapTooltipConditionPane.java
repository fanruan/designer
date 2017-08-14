package com.fr.plugin.chart.designer.other.condition.item;


import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.plugin.chart.map.line.VanChartLineMapPlotTooltipPane;

/**
 * Created by hufan on 2016/12/23.
 */
public class VanChartLineMapTooltipConditionPane extends VanChartTooltipConditionPane {

    public VanChartLineMapTooltipConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    protected VanChartPlotTooltipPane createTooltipContentsPane() {
        return new VanChartLineMapPlotTooltipPane(getPlot(), null);
    }
}
