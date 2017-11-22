package com.fr.plugin.chart.map.line;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by hufan on 2016/12/19.
 */
public class StartAndEndNameFormatPaneWithCheckBox extends CategoryNameFormatPaneWithCheckBox {
    public StartAndEndNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return Inter.getLocText("Plugin-ChartF_Start_And_End");
    }


}
