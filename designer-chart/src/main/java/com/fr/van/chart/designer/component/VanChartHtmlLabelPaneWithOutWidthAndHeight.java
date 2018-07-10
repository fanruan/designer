package com.fr.van.chart.designer.component;

import com.fr.plugin.chart.base.VanChartHtmlLabel;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/2/19.
 * 不能设置宽高
 */
public class VanChartHtmlLabelPaneWithOutWidthAndHeight extends VanChartHtmlLabelPane {
    private static final long serialVersionUID = -9213286452724939880L;

    protected JPanel createWidthAndHeightPane() {
        return new JPanel();
    }

    protected void populateWidthAndHeight(VanChartHtmlLabel htmlLabel) {
    }
    protected void updateWidthAndHeight(VanChartHtmlLabel htmlLabel) {
    }

}
