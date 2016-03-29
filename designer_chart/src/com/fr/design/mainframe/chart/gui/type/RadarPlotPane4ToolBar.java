package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.RadarIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-29
 * Time: 下午2:02
 */
public class RadarPlotPane4ToolBar extends PlotPane4ToolBar {
    private static final int RADAR = 0;

    public RadarPlotPane4ToolBar(ChartDesigner designer) {
        super(designer);
    }

    @Override
    protected String getTypeIconPath() {
        return "com/fr/design/images/toolbar/radar/";
    }

    @Override
    protected List<ChartDesignerImagePane> initDemoList() {
        List<ChartDesignerImagePane> demoList = new ArrayList<ChartDesignerImagePane>();
        ChartDesignerImagePane pane = new ChartDesignerImagePane(getTypeIconPath(), RADAR, Inter.getLocText("FR-Chart-Type_Radar"),this);
        pane.setSelected(true);
        demoList.add(pane);
        return demoList;
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] barChart = RadarIndependentChart.radarChartTypes;
        RadarPlot newPlot = (RadarPlot) barChart[this.getSelectedIndex()].getPlot();
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In RadarChart");
        }
        return cloned;
    }
}