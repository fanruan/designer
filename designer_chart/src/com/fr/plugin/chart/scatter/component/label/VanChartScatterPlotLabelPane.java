package com.fr.plugin.chart.scatter.component.label;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.label.VanChartPlotLabelPane;

import java.awt.*;

/**
 * Created by Mitisky on 16/3/1.
 */
public class VanChartScatterPlotLabelPane extends VanChartPlotLabelPane {
    private static final long serialVersionUID = 7405875523954797047L;

    public VanChartScatterPlotLabelPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected void createLabelPane() {
        labelDetailPane = new VanChartScatterPlotLabelDetailPane(this.plot, this.parent);
        labelPane.add(labelDetailPane, BorderLayout.CENTER);
    }
}
