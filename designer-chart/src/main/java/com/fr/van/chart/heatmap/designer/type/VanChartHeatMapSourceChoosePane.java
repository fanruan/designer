package com.fr.van.chart.heatmap.designer.type;

import com.fr.design.gui.ilable.UILabel;

import com.fr.van.chart.map.designer.type.VanChartMapSourceChoosePane;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanChartHeatMapSourceChoosePane extends VanChartMapSourceChoosePane {
    @Override
    protected UILabel createSourceTitleLabel() {
        return new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Map_Area_And_Point"));
    }
}
