package com.fr.van.chart.wordcloud.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.field.AbstractColumnFieldCollection;
import com.fr.chartx.data.field.diff.WordCloudColumnFieldCollection;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;
import com.fr.plugin.chart.wordcloud.WordCloudIndependentVanChart;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 16/11/29.
 */
public class VanChartWordCloudTypePane extends AbstractVanChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{
                "/com/fr/van/chart/wordcloud/images/wordcloud.png"
        };
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartWordCloudPlot.WORD_CLOUD_PLOT_ID;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Word_Cloud");
    }

    protected Plot getSelectedClonedPlot(){
        Chart chart = getDefaultChart();
        Plot newPlot = chart.getPlot();

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
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
        return WordCloudIndependentVanChart.WordCloudVanCharts[0];
    }

    @Override
    protected boolean acceptDefinition(ChartDataDefinitionProvider definition, VanChartPlot vanChartPlot) {
        if(definition instanceof AbstractDataDefinition) {
            AbstractColumnFieldCollection columnFieldCollection = ((AbstractDataDefinition) definition).getColumnFieldCollection();
            return columnFieldCollection instanceof WordCloudColumnFieldCollection;
        }
        return false;
    }
}
