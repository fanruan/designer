package com.fr.van.chart.designer.component.format;


import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by mengao on 2017/6/9.
 */
public class ChangedValueFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox{

    public ChangedValueFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Change_Value");
    }
}
