/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.AreaIndependentChart;
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
 * Time: 下午5:55
 */
public class AreaPlotPane4ToolBar extends PlotPane4ToolBar {

    private static final int STACK_AREA_CHART = 0;
    private static final int PERCENT_AREA_LINE_CHART = 1;
    private static final int STACK_3D_AREA_CHART = 2;
    private static final int PERCENT_3D_AREA_LINE_CHART = 3;

    public AreaPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/area/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();

        String area = Inter.getLocText("FR-Chart-Type_Area");
        String stack = Inter.getLocText("FR-Chart-Type_Stacked");
        String percent = Inter.getLocText("FR-Chart-Use_Percent");

        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), STACK_AREA_CHART, stack + area,this);
        pane.setSelected(true);
        demoList.add(pane);
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), PERCENT_AREA_LINE_CHART, percent + stack + area,this));

        String td = Inter.getLocText("FR-Chart-Chart_3D");
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), STACK_3D_AREA_CHART, td + stack + area,this));
        demoList.add(new ChartDesignerImagePane(getTypeIconPath(), PERCENT_3D_AREA_LINE_CHART, td + percent + stack + area,this));
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = AreaIndependentChart.areaChartTypes;
        Plot newPlot =barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In AreaChart");
        }
        return cloned;
    }
}