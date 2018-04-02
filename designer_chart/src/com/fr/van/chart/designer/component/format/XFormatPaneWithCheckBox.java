package com.fr.van.chart.designer.component.format;

import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/2/23.
 */
public class XFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {

    private static final long serialVersionUID = -782523079199004031L;

    public XFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return "x";
    }
}
