package com.fr.plugin.chart.designer.style.axis.radar;

import com.fr.plugin.chart.designer.style.axis.VanChartAxisScrollPaneWithOutTypeSelect;

/**
 * Created by Mitisky on 15/12/30.
 */
public class VanChartXAxisScrollPaneWithRadar extends VanChartAxisScrollPaneWithOutTypeSelect {
    private static final long serialVersionUID = -4329436584654335383L;

    protected void initAxisPane() {
        axisPane = new VanChartRadarXAxisPane();
    }
}