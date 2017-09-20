package com.fr.plugin.chart.line;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLinePlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewLine");
    private static final long serialVersionUID = -8161581682558781651L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/line/images/line.png",
                "/com/fr/plugin/chart/line/images/stack.png",
                "/com/fr/plugin/chart/line/images/custom.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("FR-Chart-Type_Line"),
                Inter.getLocText(new String[]{"FR-Chart-Type_Stacked","FR-Chart-Type_Line"}),
                Inter.getLocText("FR-Chart-Mode_Custom")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewLine");
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartLinePlot.VAN_CHART_LINE_PLOT;
    }

    protected Plot getSelectedClonedPlot(){
        VanChartLinePlot newPlot = null;
        Chart[] lineChart = LineIndependentVanChart.LineVanChartTypes;
        for(int i = 0, len = lineChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartLinePlot)lineChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In LineChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return LineIndependentVanChart.LineVanChartTypes[0];
    }
}