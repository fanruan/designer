/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.PieIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午5:52
 */
public class PiePlotPane4ToolBar extends PlotPane4ToolBar {
    private static final int PIE_CHART = 0;
    private static final int THREE_D_PIE_CHART = 1;

    public PiePlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/pie/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), PIE_CHART, Inter.getLocText("I-PieStyle_Normal"),this);
        pane.setSelected(true);
        demoList.add(pane);
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), THREE_D_PIE_CHART,
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D", "I-PieStyle_Normal"}),this));
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = PieIndependentChart.pieChartTypes;
        PiePlot newPlot = (PiePlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In PieChart");
        }
        return cloned;
    }
}