package com.fr.plugin.chart.column;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 15/9/24.
 */
public class VanChartColumnPlotPane extends AbstractVanChartTypePane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewColumn");

    private static final long serialVersionUID = 5950923001789733745L;


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/column/images/column.png",
                "/com/fr/plugin/chart/column/images/stack.png",
                "/com/fr/plugin/chart/column/images/percentstack.png",
                "/com/fr/plugin/chart/column/images/custom.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        String column = Inter.getLocText("FR-Chart-Type_Column");
        String stack = Inter.getLocText("FR-Chart-Type_Stacked");
        String percent = Inter.getLocText("FR-Chart-Use_Percent");
        return new String[]{
                column,
                stack + column,
                percent + stack + column,
                Inter.getLocText("FR-Chart-Mode_Custom")
        };
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewColumn");
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
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In ColumnChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return ColumnIndependentVanChart.ColumnVanChartTypes[0];
    }

}