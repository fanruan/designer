package com.fr.van.chart.pie;


import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;

import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.pie.PieIndependentVanChart;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * 饼图(新特性) 属性表 选择类型 布局界面.
 */
public class VanChartPiePlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Pie");

    private static final long serialVersionUID = 6163246902689597259L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/pie/images/pie.png",
                "/com/fr/van/chart/pie/images/same.png",
                "/com/fr/van/chart/pie/images/different.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Pie"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_SameArc_Pie"),
                com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_DifferentArcPie")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Pie");
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return PiePlot4VanChart.VAN_CHART_PIE_PLOT;
    }

    protected Plot getSelectedClonedPlot(){
        PiePlot4VanChart newPlot = null;
        Chart[] pieChart = PieIndependentVanChart.newPieChartTypes;
        for(int i = 0, len = pieChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (PiePlot4VanChart)pieChart[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In PieChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return PieIndependentVanChart.newPieChartTypes[0];
    }
}