package com.fr.van.chart.drillmap.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;

import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.drillmap.DrillMapIndependentVanChart;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.type.ZoomLevel;
import com.fr.van.chart.map.designer.type.VanChartMapPlotPane;
import com.fr.van.chart.map.designer.type.VanChartMapSourceChoosePane;

import java.util.ArrayList;

/**
 * Created by Mitisky on 16/6/20.
 */
public class VanChartDrillMapPlotPane extends VanChartMapPlotPane {

    public static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Drill_Map");

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

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region_Map"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_PointMap"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Map")
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
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Drill_Map");
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
