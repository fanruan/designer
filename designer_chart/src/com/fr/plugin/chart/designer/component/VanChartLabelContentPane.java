package com.fr.plugin.chart.designer.component;

import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

public class VanChartLabelContentPane extends VanChartTooltipContentPane {

    private static final long serialVersionUID = 5630276526789839288L;

    public VanChartLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected VanChartHtmlLabelPane createHtmlLabelPane() {
        return new VanChartHtmlLabelPane();
    }
}
