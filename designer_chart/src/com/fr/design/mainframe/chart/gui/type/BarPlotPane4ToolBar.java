/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.BarIndependentChart;
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
 * Time: 下午5:44
 */
public class BarPlotPane4ToolBar extends PlotPane4ToolBar {
    private static final int COLOMN_CHART = 0;
    private static final int STACK_COLOMN_CHART = 1;
    private static final int PERCENT_STACK_COLOMN_CHART = 2;
    private static final int THREE_D_COLOMN_CHART = 3;
    private static final int THREE_D_COLOMN_HORIZON_DRAW_CHART = 4;
    private static final int THREE_D_STACK_COLOMN_CHART = 5;
    private static final int THREE_D_PERCENT_STACK_COLOMN_CHART = 6;

    public BarPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/bar/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), COLOMN_CHART, Inter.getLocText("FR-Chart-Type_Bar"),this);
        pane.setSelected(true);
        demoList.add(pane);
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), STACK_COLOMN_CHART, Inter.getLocText(new String[]{"FR-Chart-Type_Stacked","FR-Chart-Type_Bar"}),this));
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), PERCENT_STACK_COLOMN_CHART, Inter.getLocText(new String[]{"FR-Chart-Use_Percent","FR-Chart-Type_Stacked","FR-Chart-Type_Bar"}),this));

        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), THREE_D_COLOMN_CHART, Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Bar"}),this));

        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), THREE_D_COLOMN_HORIZON_DRAW_CHART, Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Bar","FR-Chart-Direction_Horizontal"},new String[]{"","(",")"}),this));

        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), THREE_D_STACK_COLOMN_CHART,
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Type_Stacked","FR-Chart-Type_Bar"}),this));
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), THREE_D_PERCENT_STACK_COLOMN_CHART,
                Inter.getLocText(new String[]{"FR-Chart-Chart_3D","FR-Chart-Use_Percent","FR-Chart-Type_Stacked","FR-Chart-Type_Bar"}),this));
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = BarIndependentChart.barChartTypes;
        BarPlot newPlot = (BarPlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In BarChart");
        }
        return cloned;
    }
}