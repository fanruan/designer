package com.fr.plugin.chart.drillmap.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.drillmap.DrillMapIndependentVanChart;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.type.ZoomLevel;
import com.fr.plugin.chart.map.designer.type.VanChartMapPlotPane;
import com.fr.plugin.chart.map.designer.type.VanChartMapSourceChoosePane;

import java.util.ArrayList;

/**
 * Created by Mitisky on 16/6/20.
 */
public class VanChartDrillMapPlotPane extends VanChartMapPlotPane {

    public static final String TITLE = Inter.getLocText("Plugin-ChartF_Drill_Map");

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/drillmap/images/area-map.png",
                "/com/fr/plugin/chart/drillmap/images/point-map.png",
                "/com/fr/plugin/chart/drillmap/images/custom-map.png"
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

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_AreaMap"),
                Inter.getLocText("Plugin-ChartF_PointMap"),
                Inter.getLocText("Plugin-ChartF_CustomDrillMap")
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
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartDrillMapPlot.VAN_CHART_DRILL_MAP_ID;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_Drill_Map");
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
}
