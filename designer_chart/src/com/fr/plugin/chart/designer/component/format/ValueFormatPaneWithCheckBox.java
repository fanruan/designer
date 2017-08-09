package com.fr.plugin.chart.designer.component.format;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Mitisky on 16/2/23.
 */
public class ValueFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {

    private static final long serialVersionUID = -8793617093976412625L;

    public ValueFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return Inter.getLocText("Chart-Use_Value");
    }
}
