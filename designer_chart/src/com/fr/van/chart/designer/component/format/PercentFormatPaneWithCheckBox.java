package com.fr.van.chart.designer.component.format;

import com.fr.general.Inter;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/2/23.
 */
public class PercentFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {

    private static final long serialVersionUID = 566737138492111631L;

    public PercentFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return Inter.getLocText("Chart-Use_Percent");
    }

    protected boolean isPercent() {
        return true;
    }
}
