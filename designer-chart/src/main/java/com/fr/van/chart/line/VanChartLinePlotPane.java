package com.fr.van.chart.line;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;

import com.fr.plugin.chart.line.LineIndependentVanChart;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLinePlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_NewLine");
    private static final long serialVersionUID = -8161581682558781651L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/line/images/line.png",
                "/com/fr/van/chart/line/images/stack.png",
                "/com/fr/van/chart/line/images/custom.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Type_Line"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stacked_Line"),
                com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Mode_Custom")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_NewLine");
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
            FineLoggerFactory.getLogger().error("Error In LineChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return LineIndependentVanChart.LineVanChartTypes[0];
    }
}