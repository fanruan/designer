package com.fr.plugin.chart.gantt.designer.style.tooltip;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.plugin.chart.gantt.attr.AttrGanttTooltip;
import com.fr.plugin.chart.gantt.attr.AttrGanttTooltipContent;

import javax.swing.*;

/**
 * Created by hufan on 2017/1/17.
 */
public class VanChartGanttPlotTooltipPane extends VanChartPlotTooltipPane {
    public VanChartGanttPlotTooltipPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    @Override
    protected JPanel createDisplayStrategy(Plot plot) {
        return null;
    }

    protected AttrTooltip getAttrTooltip() {
        AttrGanttTooltip attrGanttTooltip = new AttrGanttTooltip();
        ((AttrGanttTooltipContent)attrGanttTooltip.getContent()).getDurationFormat().setEnable(true);
        return attrGanttTooltip;
    }
}
