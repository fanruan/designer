package com.fr.van.chart.scatter.component.tooltip;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;

import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.scatter.VanChartScatterPlotTooltipPane;

import java.awt.BorderLayout;

/**
 * Created by Mitisky on 15/8/29.
 */
public class VanChartScatterPlotTooltipNoCheckPane extends VanChartScatterPlotTooltipPane {

    public VanChartScatterPlotTooltipNoCheckPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected  void addComponents(Plot plot) {
        isTooltipShow = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Tooltip"));
        tooltipPane = createTooltipPane(plot);

        this.setLayout(new BorderLayout());
        this.add(tooltipPane,BorderLayout.CENTER);
    }

    @Override
    public void populate(AttrTooltip attr) {
        super.populate(attr);
        isTooltipShow.setSelected(true);
        tooltipPane.setEnabled(isTooltipShow.isSelected());
    }

}