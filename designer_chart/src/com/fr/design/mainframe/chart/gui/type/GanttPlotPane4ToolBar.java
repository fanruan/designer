package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.GanttIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : DAISY
 * Version: 6.5.6
 * Date: 14-10-29
 * Time: 下午2:16
 */
public class GanttPlotPane4ToolBar extends PlotPane4ToolBar {

    private static final int GANTT = 0;

    public GanttPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/gantt/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), GANTT, Inter.getLocText("FR-Chart-Type_Gantt"),this);
        pane.setSelected(true);
        demoList.add(pane);
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = GanttIndependentChart.ganttChartTypes;
        GanttPlot newPlot = (GanttPlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In GanttChart");
        }
        return cloned;
    }
}