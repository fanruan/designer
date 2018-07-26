package com.fr.van.chart.radar;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;

import com.fr.plugin.chart.radar.RadarIndependentVanChart;
import com.fr.plugin.chart.radar.VanChartRadarPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarPlotPane  extends AbstractVanChartTypePane {
    public static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_NewRadar");

    private static final long serialVersionUID = -4599483879031804911L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/radar/images/radar.png",
                "/com/fr/van/chart/radar/images/stack.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Radar"),
                com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_StackColumnTypeRadar")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_NewRadar");
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartRadarPlot.VAN_CHART_RADAR_PLOT;
    }

    protected Plot getSelectedClonedPlot(){
        VanChartRadarPlot newPlot = null;
        Chart[] RadarChart = RadarIndependentVanChart.RadarVanChartTypes;
        for(int i = 0, len = RadarChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartRadarPlot)RadarChart[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In RadarChart");
        }
        return cloned;
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
    }

    public Chart getDefaultChart() {
        return RadarIndependentVanChart.RadarVanChartTypes[0];
    }

}