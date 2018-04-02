package com.fr.van.chart.designer.component;

import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

public class VanChartLabelContentPane extends VanChartTooltipContentPane {

    private static final long serialVersionUID = 5630276526789839288L;

    public VanChartLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected VanChartHtmlLabelPane createHtmlLabelPane() {
        return new VanChartHtmlLabelPane();
    }
}
