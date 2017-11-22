package com.fr.plugin.chart.funnel.designer.style;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Mitisky on 16/10/10.
 */
public class FunnelPercentFormatPaneWithCheckBox extends PercentFormatPaneWithCheckBox {
    public FunnelPercentFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected String getCheckBoxText() {
        return Inter.getLocText("Chart-Value_Conversion");
    }
}
