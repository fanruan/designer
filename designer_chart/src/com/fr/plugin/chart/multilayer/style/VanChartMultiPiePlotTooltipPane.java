package com.fr.plugin.chart.multilayer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;

/**
 * Created by Fangjie on 2016/7/1.
 */
public class VanChartMultiPiePlotTooltipPane extends VanChartPlotTooltipPane {

    //多层饼图显示所有层级使用原来的显示所有系列按钮
    public VanChartMultiPiePlotTooltipPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected String getShowAllSeriesLabelText() {
        return Inter.getLocText("Plugin-ChartF_ShowAllLevel");
    }
}
