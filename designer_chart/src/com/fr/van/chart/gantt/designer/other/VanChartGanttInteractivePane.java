package com.fr.van.chart.gantt.designer.other;

import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.other.AutoRefreshPane;
import com.fr.van.chart.designer.other.AutoRefreshPaneWithoutTooltip;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithOutSort;

/**
 * Created by mengao on 2017/6/21.
 */
public class VanChartGanttInteractivePane extends VanChartInteractivePaneWithOutSort {

    protected AutoRefreshPane getMoreLabelPane(VanChartPlot plot) {
        boolean isLargeModel = largeModel(plot);
        return new AutoRefreshPaneWithoutTooltip((VanChart) chart, isLargeModel);
    }
}
