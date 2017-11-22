package com.fr.plugin.chart.funnel.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;
import com.fr.plugin.chart.funnel.FunnelIndependentVanChart;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;

/**
 * Created by Mitisky on 16/10/10.
 */
public class VanChartFunnelTypePane extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewFunnel");

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{
                "/com/fr/plugin/chart/funnel/images/funnel.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("FR-Chart-Type_Funnel")
        };
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartFunnelPlot.VAN_CHART_FUNNEL_PLOT_ID;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewFunnel");
    }

    protected Plot getSelectedClonedPlot(){
        Chart chart = getDefaultChart();
        VanChartFunnelPlot newPlot = (VanChartFunnelPlot) chart.getPlot();

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
        return tools;
    }

    public Chart getDefaultChart() {
        return FunnelIndependentVanChart.FunnelVanChartTypes[0];
    }
}
