package com.fr.van.chart.designer.style.axis.bar;

import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.axis.VanChartAxisPane;
import com.fr.van.chart.designer.style.axis.VanChartXYAxisPaneInterface;

/**
 * Created by Mitisky on 16/6/8.
 */
public class VanChartBarAxisPane extends VanChartAxisPane {
    public VanChartBarAxisPane(VanChartAxisPlot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected VanChartXYAxisPaneInterface getDefaultXAxisScrollPane() {
        return new VanChartXAxisScrollPaneWithOutTypeSelect();
    }

    @Override
    protected VanChartXYAxisPaneInterface getDefaultYAxisScrollPane() {
        return new VanChartYAxisScrollPaneWithTypeSelect();
    }

}
