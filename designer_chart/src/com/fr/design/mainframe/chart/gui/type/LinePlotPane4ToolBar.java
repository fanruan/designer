/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.LineIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : DAISY
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午5:37
 */
public class LinePlotPane4ToolBar extends PlotPane4ToolBar {


    private static final int LINE_CHART = 0;

    public LinePlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/line/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), LINE_CHART, Inter.getLocText("I-LineStyle_Line"), this);
        pane.setSelected(true);
        demoList.add(pane);
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = LineIndependentChart.lineChartTypes;
        LinePlot newPlot = (LinePlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In LineChart");
        }
        return cloned;
    }
}