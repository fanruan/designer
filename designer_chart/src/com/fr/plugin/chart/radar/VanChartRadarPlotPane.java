package com.fr.plugin.chart.radar;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarPlotPane  extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewRadar");

    private static final long serialVersionUID = -4599483879031804911L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/radar/images/radar.png",
                "/com/fr/plugin/chart/radar/images/stack.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_Radar"),
                Inter.getLocText("Plugin-ChartF_StackColumnTypeRadar")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewRadar");
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
            FRLogger.getLogger().error("Error In RadarChart");
        }
        return cloned;
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
    }

    public Chart getDefaultChart() {
        return RadarIndependentVanChart.RadarVanChartTypes[0];
    }

}