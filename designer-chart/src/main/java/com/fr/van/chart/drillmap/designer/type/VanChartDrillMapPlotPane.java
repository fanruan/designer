package com.fr.van.chart.drillmap.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.drillmap.DrillMapIndependentVanChart;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.drillmap.data.DrillMapDefinition;
import com.fr.plugin.chart.map.MapMatchResult;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.type.ZoomLevel;
import com.fr.van.chart.map.designer.type.VanChartMapPlotPane;
import com.fr.van.chart.map.designer.type.VanChartMapSourceChoosePane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 16/6/20.
 */
public class VanChartDrillMapPlotPane extends VanChartMapPlotPane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/drillmap/images/area-map.png",
                "/com/fr/van/chart/drillmap/images/point-map.png",
                "/com/fr/van/chart/drillmap/images/custom-map.png"
        };
    }

    @Override
    protected VanChartMapSourceChoosePane createSourceChoosePane() {
        return new VanChartMapSourceChoosePane(){
            @Override
            protected boolean supportParam(){
                return false;
            }
        };
    }


    //钻取地图不全屏
    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        tools.setExport(false);
        tools.setFullScreen(false);
        return tools;
    }

    /**
     * 同一个钻取地图， 类型之间切换
     */
    @Override
    protected void cloneOldPlot2New(Plot oldPlot, Plot newPlot) {
        super.cloneOldPlot2New(oldPlot, newPlot);

        try {
            List<MapMatchResult> newMatchResultList = new ArrayList<>();
            List<MapMatchResult> matchResultList = ((VanChartDrillMapPlot) oldPlot).getMatchResultList();
            if (matchResultList != null) {
                for (MapMatchResult matchResult : matchResultList) {
                    newMatchResultList.add((MapMatchResult) matchResult.clone());
                }
            }
            ((VanChartDrillMapPlot) newPlot).setMatchResultList(newMatchResultList);
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error in change plot");
        }
    }

    protected Chart[] getDefaultCharts() {
        return DrillMapIndependentVanChart.DrillMapVanCharts;
    }

    public Chart getDefaultChart() {
        return DrillMapIndependentVanChart.DrillMapVanCharts[0];
    }

    protected void resetAttr(Plot plot) {
        super.resetAttr(plot);
        if(plot instanceof VanChartDrillMapPlot) {
            resetLayerTypeAndZoomLevel((VanChartDrillMapPlot) plot);
        }
    }

    private void resetLayerTypeAndZoomLevel(VanChartDrillMapPlot drillMapPlot) {
        drillMapPlot.setLayerLevelList(new ArrayList<ZoomLevel>());
        drillMapPlot.setLayerMapTypeList(new ArrayList<MapType>());
    }

    @Override
    protected boolean acceptDefinition(ChartDataDefinitionProvider definition, VanChartPlot vanChartPlot) {
        return definition instanceof DrillMapDefinition;
    }
}
