package com.fr.plugin.chart.designer.component.format;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by mengao on 2017/6/9.
 */
public class ChangedValueFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox{

    public ChangedValueFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return Inter.getLocText("Plugin-ChartF_Change_Value");
    }
}
