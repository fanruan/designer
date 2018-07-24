package com.fr.van.chart.designer.component.format;


import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/2/23.
 */
public class CategoryNameFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {

    private static final long serialVersionUID = -782523079199004032L;

    public CategoryNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Chart-Category_Name");
    }
}
