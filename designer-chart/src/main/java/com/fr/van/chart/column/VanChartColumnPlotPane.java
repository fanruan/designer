package com.fr.van.chart.column;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;

import com.fr.plugin.chart.column.ColumnIndependentVanChart;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/9/24.
 */
public class VanChartColumnPlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Column");

    private static final long serialVersionUID = 5950923001789733745L;


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/column/images/column.png",
                "/com/fr/van/chart/column/images/stack.png",
                "/com/fr/van/chart/column/images/percentstack.png",
                "/com/fr/van/chart/column/images/custom.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        String column = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Column");
        String stack = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stacked");
        String percent = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Percent");
        return new String[]{
                column,
                stack + column,
                percent + stack + column,
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Mode_Custom")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_Column");
    }


    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartColumnPlot.VAN_CHART_COLUMN_PLOT_ID;
    }

    protected Plot getSelectedClonedPlot(){
        VanChartColumnPlot newPlot = null;
        Chart[] columnChart = ColumnIndependentVanChart.ColumnVanChartTypes;
        for(int i = 0, len = columnChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartColumnPlot)columnChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
             if(newPlot != null) {
                 cloned = (Plot) newPlot.clone();
             }
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In ColumnChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return ColumnIndependentVanChart.ColumnVanChartTypes[0];
    }

}