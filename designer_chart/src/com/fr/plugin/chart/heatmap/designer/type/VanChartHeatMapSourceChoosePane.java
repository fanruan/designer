package com.fr.plugin.chart.heatmap.designer.type;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.map.designer.type.VanChartMapSourceChoosePane;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanChartHeatMapSourceChoosePane extends VanChartMapSourceChoosePane {
    @Override
    protected UILabel createSourceTitleLabel() {
        return new UILabel(Inter.getLocText("Plugin-ChartF_Map_Area_And_Point"));
    }
}
