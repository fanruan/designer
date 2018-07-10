package com.fr.van.chart.area;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.general.Inter;
import com.fr.plugin.chart.area.AreaIndependentVanChart;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/11/18.
 */
public class VanChartAreaPlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewArea");
    private static final long serialVersionUID = -8161581682558781651L;

    @Override
    protected String[] getTypeIconPath() {

        return new String[]{"/com/fr/van/chart/area/images/area.png",
                "/com/fr/van/chart/area/images/stack.png",
                "/com/fr/van/chart/area/images/percentStack.png",
                "/com/fr/van/chart/area/images/custom.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        String area = Inter.getLocText("FR-Chart-Type_Area");
        String stack = Inter.getLocText("FR-Chart-Type_Stacked");
        String percent = Inter.getLocText("FR-Chart-Use_Percent");
        return new String[]{
                area,
                stack + area,
                percent + stack + area,
                Inter.getLocText("FR-Chart-Mode_Custom")
        };
    }

    /**
     * 返回界面标题
     *
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewArea");
    }



    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartAreaPlot.VAN_CHART_AREA_PLOT_ID;
    }

    protected Plot getSelectedClonedPlot() {
        VanChartAreaPlot newPlot = null;
        Chart[] areaChart = AreaIndependentVanChart.AreaVanChartTypes;
        for (int i = 0, len = areaChart.length; i < len; i++) {
            if (typeDemo.get(i).isPressing) {
                newPlot = (VanChartAreaPlot) areaChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In AreaChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return AreaIndependentVanChart.AreaVanChartTypes[0];
    }

}