package com.fr.van.chart.gantt.designer.style;

import com.fr.van.chart.designer.component.VanChartHtmlLabelPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.gantt.designer.style.tooltip.VanChartGanttTooltipContentPane;

import javax.swing.JPanel;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartGanttLabelContentPane extends VanChartGanttTooltipContentPane {
    public VanChartGanttLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected VanChartHtmlLabelPane createHtmlLabelPane() {
        return new VanChartHtmlLabelPane();
    }
}
