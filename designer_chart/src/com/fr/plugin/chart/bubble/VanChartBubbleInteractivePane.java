package com.fr.plugin.chart.bubble;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithOutSort;

import javax.swing.*;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleInteractivePane extends VanChartInteractivePaneWithOutSort {
    protected String[] getNameArray() {
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartBubblePlot && ((VanChartBubblePlot) plot).isForceBubble()) {
            return new String[]{Inter.getLocText("Plugin-ChartF_XYAxis"), Inter.getLocText("Chart-Use_None")};
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

    @Override
    protected JPanel getzoomTypePane(UIButtonGroup zoomType) {
        if (((VanChartBubblePlot)chart.getPlot()).isForceBubble()) {
            return TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_ZoomType"), zoomType);
        }
        return super.getzoomTypePane(zoomType);
    }
}
