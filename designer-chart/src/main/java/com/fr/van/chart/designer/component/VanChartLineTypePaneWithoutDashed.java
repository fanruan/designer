package com.fr.van.chart.designer.component;

import com.fr.plugin.chart.type.LineType;

public class VanChartLineTypePaneWithoutDashed extends VanChartLineTypePane{

    @Override
    protected LineTypeComboBox createLineType() {
        return new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.SOLID});
    }
}
