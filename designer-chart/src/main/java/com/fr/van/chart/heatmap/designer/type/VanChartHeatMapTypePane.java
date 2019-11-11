package com.fr.van.chart.heatmap.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.heatmap.HeatMapIndependentVanChart;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.map.server.CompatibleGEOJSONHelper;
import com.fr.van.chart.map.designer.type.VanChartMapPlotPane;
import com.fr.van.chart.map.designer.type.VanChartMapSourceChoosePane;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanChartHeatMapTypePane extends VanChartMapPlotPane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/heatmap/images/heatmap.png"
        };
    }

    @Override
    protected VanChartMapSourceChoosePane createSourceChoosePane() {
        return new VanChartHeatMapSourceChoosePane();
    }

    //适用一种图表只有一种类型的
    public void populateBean(Chart chart) {
        typeDemo.get(0).isPressing = true;
        VanChartHeatMapPlot plot = (VanChartHeatMapPlot)chart.getPlot();
        populateSourcePane(plot);

        boolean enabled = !CompatibleGEOJSONHelper.isDeprecated(plot.getGeoUrl());
        GUICoreUtils.setEnabled(this.getTypePane(), enabled);
        GUICoreUtils.setEnabled(this.getSourceChoosePane().getSourceComboBox(), enabled);

        checkDemosBackground();
    }

    //热力地图不全屏
    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        tools.setExport(false);
        tools.setFullScreen(false);
        return tools;
    }

    protected Plot getSelectedClonedPlot(){
        Chart chart = getDefaultChart();
        VanChartHeatMapPlot newPlot = (VanChartHeatMapPlot)chart.getPlot();

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return cloned;
    }

    protected Chart[] getDefaultCharts() {
        return HeatMapIndependentVanChart.HeatMapVanCharts;
    }

    public Chart getDefaultChart() {
        return HeatMapIndependentVanChart.HeatMapVanCharts[0];
    }
}
