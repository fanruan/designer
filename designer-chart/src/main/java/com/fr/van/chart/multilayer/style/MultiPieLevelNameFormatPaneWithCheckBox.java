package com.fr.van.chart.multilayer.style;

import com.fr.general.Inter;
import com.fr.van.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

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
