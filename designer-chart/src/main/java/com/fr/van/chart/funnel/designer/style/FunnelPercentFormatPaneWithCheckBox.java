package com.fr.van.chart.funnel.designer.style;


import com.fr.van.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/10/10.
 */
public class FunnelPercentFormatPaneWithCheckBox extends PercentFormatPaneWithCheckBox {
    public FunnelPercentFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Conversion");
    }
}
