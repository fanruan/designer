package com.fr.plugin.chart.bubble.force;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.label.VanChartLabelPane;
import com.fr.plugin.chart.designer.style.label.VanChartPlotLabelPane;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartForceBubbleLabelPane extends VanChartLabelPane {
    public VanChartForceBubbleLabelPane(VanChartStylePane parent) {
        super(parent);
    }

    protected VanChartPlotLabelPane getLabelPane(Plot plot) {
        return new VanChartPlotLabelPane(plot, parent);
    }
}
