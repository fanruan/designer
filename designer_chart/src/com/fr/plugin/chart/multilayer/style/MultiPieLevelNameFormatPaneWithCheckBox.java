package com.fr.plugin.chart.multilayer.style;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class MultiPieLevelNameFormatPaneWithCheckBox extends CategoryNameFormatPaneWithCheckBox {
    public MultiPieLevelNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return Inter.getLocText("Plugin-ChartF_Level_Name");
    }
}
