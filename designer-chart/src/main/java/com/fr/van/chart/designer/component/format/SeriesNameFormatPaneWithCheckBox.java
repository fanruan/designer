package com.fr.van.chart.designer.component.format;


import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/2/23.
 */
public class SeriesNameFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {

    private static final long serialVersionUID = 6456517419221601327L;

    public SeriesNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name");
    }
}
