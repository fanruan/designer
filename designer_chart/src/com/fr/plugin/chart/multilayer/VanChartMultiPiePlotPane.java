package com.fr.plugin.chart.multilayer;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class VanChartMultiPiePlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewMultiPie");
    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/multilayer/image/multiPie.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_MultiPieChart")
        };
    }

    @Override
    protected String getPlotTypeID() {
        return VanChartMultiPiePlot.VAN_CHART_MULTILAYER_PLOT_ID;
    }

    @Deprecated
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewMultiPie");
    }

    @Override
    protected Plot getSelectedClonedPlot(){
        VanChartMultiPiePlot newPlot = null;
        Chart[] multilayerCharts = MultiPieIndependentVanChart.MultilayerVanChartTypes;
        for(int i = 0, len = multilayerCharts.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartMultiPiePlot) multilayerCharts[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In MultiPieChart");
        }
        return cloned;
    }

    /**
     * 防止新建其他图表从而切换很卡
     * @return
     */
    public Chart getDefaultChart() {
        return MultiPieIndependentVanChart.MultilayerVanChartTypes[0];
    }

    protected void resetChartAttr(Chart chart, Plot newPlot){
        super.resetChartAttr(chart, newPlot);
        //重置工具栏选项
        VanChartTools tools = ((VanChart) chart).getVanChartTools();
        if (tools != null) {
            tools.setSort(false);
            tools.setFullScreen(false);
            tools.setExport(false);
        }
    }
}
