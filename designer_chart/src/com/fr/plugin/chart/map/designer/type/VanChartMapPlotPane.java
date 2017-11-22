package com.fr.plugin.chart.map.designer.type;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;
import com.fr.plugin.chart.map.MapIndependentVanChart;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.server.CompatibleGEOJSONHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/5/4.
 */
public class VanChartMapPlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewMap");

    private VanChartMapSourceChoosePane sourceChoosePane;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/map/images/area-map.png",
                "/com/fr/plugin/chart/map/images/point-map.png",
                "/com/fr/plugin/chart/map/images/line-map.png",
                "/com/fr/plugin/chart/map/images/custom-map.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_AreaMap"),
                Inter.getLocText("Plugin-ChartF_PointMap"),
                Inter.getLocText("Plugin-ChartF_LineMap"),
                Inter.getLocText("Plugin-ChartF_CustomMap")
        };
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartMapPlot.VAN_CHART_MAP_ID;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewMap");
    }

    protected Component[][] getPaneComponents(JPanel typePane){
        try {
            sourceChoosePane = createSourceChoosePane();
        } catch (Exception e){
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return new Component[][]{
                new Component[]{typePane},
                new Component[]{sourceChoosePane}
        };
    }

    protected VanChartMapSourceChoosePane createSourceChoosePane() {
        return new VanChartMapSourceChoosePane();
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        VanChartMapPlot plot = (VanChartMapPlot)chart.getPlot();

        typeDemo.get(plot.getDetailType()).isPressing = true;
        populateSourcePane(plot);

        boolean enabled = !CompatibleGEOJSONHelper.isDeprecated(plot.getGeoUrl());
        GUICoreUtils.setEnabled(this.getTypePane(), enabled);
        GUICoreUtils.setEnabled(this.sourceChoosePane.getSourceComboBox(), enabled);

        checkDemosBackground();
    }

    protected void populateSourcePane(VanChartMapPlot plot) {
        //populate需要使用clone的plot
        try {
            VanChartMapPlot mapPlot = (VanChartMapPlot)plot.clone();
            sourceChoosePane.populateBean(mapPlot);
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    public void updateBean(Chart chart) {
        super.updateBean(chart);
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartMapPlot) {
            sourceChoosePane.updateBean((VanChartMapPlot) plot);
            if(!samePlot || (typeChanged && samePlot)){
                resetAttr(plot);
            }
        }
    }

    /**
     * 不同地图类型的超链不需要复制
     * @param oldPlot
     * @param newPlot
     * @throws CloneNotSupportedException
     */
    protected void cloneHotHyperLink(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{

    }

    protected void resetAttr(Plot plot) {
        sourceChoosePane.resetComponentValue((VanChartMapPlot) plot);
    }

    protected Plot getSelectedClonedPlot(){
        VanChartMapPlot newPlot = null;
        Chart[] charts = getDefaultCharts();
        for(int i = 0, len = charts.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartMapPlot) charts[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return cloned;
    }

    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        tools.setExport(false);
        return tools;
    }

    protected Chart[] getDefaultCharts() {
        return MapIndependentVanChart.MapVanCharts;
    }

    public Chart getDefaultChart() {
        return MapIndependentVanChart.MapVanCharts[0];
    }

    public VanChartMapSourceChoosePane getSourceChoosePane(){
        return this.sourceChoosePane;
    }

}
