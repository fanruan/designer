package com.fr.plugin.chart.gantt.designer.other;

import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.designer.other.AutoRefreshPane;
import com.fr.plugin.chart.designer.other.AutoRefreshPaneWithoutTooltip;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by mengao on 2017/6/21.
 */
public class VanChartGanttInteractivePane extends VanChartInteractivePaneWithOutSort{

    protected AutoRefreshPane getMoreLabelPane(VanChartPlot plot) {
        boolean isLargeModel = largeModel(plot);
        return new AutoRefreshPaneWithoutTooltip((VanChart) chart, isLargeModel);
    }
}
