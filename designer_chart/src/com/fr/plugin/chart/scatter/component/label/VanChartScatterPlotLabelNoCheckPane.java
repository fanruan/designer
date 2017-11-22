package com.fr.plugin.chart.scatter.component.label;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import java.awt.*;

/**
 * 条件属性中用到
 */
public class VanChartScatterPlotLabelNoCheckPane extends VanChartScatterPlotLabelPane {

    private static final long serialVersionUID = 8124894034484334810L;

    public VanChartScatterPlotLabelNoCheckPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected void addComponents() {
        this.setLayout(new BorderLayout());
        this.add(labelPane,BorderLayout.CENTER);
    }

    public void populate(AttrLabel attr) {
        super.populate(attr);
        isLabelShow.setSelected(true);
        labelPane.setVisible(true);
    }
}