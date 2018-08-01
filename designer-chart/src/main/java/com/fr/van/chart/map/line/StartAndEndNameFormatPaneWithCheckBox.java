package com.fr.van.chart.map.line;


import com.fr.van.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by hufan on 2016/12/19.
 */
public class StartAndEndNameFormatPaneWithCheckBox extends CategoryNameFormatPaneWithCheckBox {
    public StartAndEndNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_And_End");
    }


}
