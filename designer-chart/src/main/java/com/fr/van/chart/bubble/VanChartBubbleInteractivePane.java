package com.fr.van.chart.bubble;

import com.fr.chart.chartattr.Plot;

import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithOutSort;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleInteractivePane extends VanChartInteractivePaneWithOutSort {
    protected String[] getNameArray() {
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartBubblePlot && ((VanChartBubblePlot) plot).isForceBubble()) {
            return new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_XY_Axis"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None")};
        }
        return super.getNameArray();
    }

    protected String[] getValueArray() {
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartBubblePlot && ((VanChartBubblePlot) plot).isForceBubble()) {
            return new String[]{VanChartConstants.ZOOM_TYPE_XY, VanChartConstants.ZOOM_TYPE_NONE};
        }
        return super.getValueArray();
    }

}
