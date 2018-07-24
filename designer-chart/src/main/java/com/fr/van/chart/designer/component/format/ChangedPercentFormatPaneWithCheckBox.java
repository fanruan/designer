package com.fr.van.chart.designer.component.format;


import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by mengao on 2017/6/5.
 */
public class ChangedPercentFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {

    public ChangedPercentFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Change_Percent");
    }

    protected boolean isPercent() {
        return true;
    }
}
