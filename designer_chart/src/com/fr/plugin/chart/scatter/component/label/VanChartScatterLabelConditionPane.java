package com.fr.plugin.chart.scatter.component.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.plugin.chart.designer.style.label.VanChartPlotLabelPane;

public class VanChartScatterLabelConditionPane extends VanChartLabelConditionPane {

    public VanChartScatterLabelConditionPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    @Override
    protected VanChartPlotLabelPane createLabelPane() {
        return new VanChartScatterPlotLabelNoCheckPane(getPlot(), null);
    }
}