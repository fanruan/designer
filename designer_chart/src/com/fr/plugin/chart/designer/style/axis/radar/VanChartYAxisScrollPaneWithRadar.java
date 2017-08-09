package com.fr.plugin.chart.designer.style.axis.radar;

import com.fr.plugin.chart.designer.style.axis.VanChartAxisScrollPaneWithOutTypeSelect;

/**
 * Created by Mitisky on 15/12/30.
 */
public class VanChartYAxisScrollPaneWithRadar extends VanChartAxisScrollPaneWithOutTypeSelect {
    private static final long serialVersionUID = -3570661012560445014L;

    protected void initAxisPane() {
        axisPane = new VanChartRadarYAxisPane();
    }
}