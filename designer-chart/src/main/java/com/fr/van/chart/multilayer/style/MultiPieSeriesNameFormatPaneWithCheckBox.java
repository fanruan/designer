package com.fr.van.chart.multilayer.style;


import com.fr.van.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class MultiPieSeriesNameFormatPaneWithCheckBox extends SeriesNameFormatPaneWithCheckBox {
    private static final long serialVersionUID = 6456517419221601327L;

    public MultiPieSeriesNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_MultiPie_Series_Name");
    }
}
