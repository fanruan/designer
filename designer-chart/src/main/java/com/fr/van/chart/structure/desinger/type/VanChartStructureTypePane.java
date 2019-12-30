package com.fr.van.chart.structure.desinger.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.field.AbstractColumnFieldCollection;
import com.fr.chartx.data.field.diff.StructureColumnFieldCollection;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.structure.StructureIndependentVanChart;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.wordcloud.WordCloudIndependentVanChart;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureTypePane extends AbstractVanChartTypePane {
    @Override
    protected String[] getTypeIconPath() {
        return new String[]{
                "/com/fr/van/chart/structure/images/vertical.png",
                "/com/fr/van/chart/structure/images/horizontal.png",
                "/com/fr/van/chart/structure/images/radial.png"
        };
    }

    protected Plot getSelectedClonedPlot(){
        VanChartStructurePlot newPlot = null;
        Chart[] charts = StructureIndependentVanChart.StructureVanCharts;
        for(int i = 0, len = charts.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartStructurePlot)charts[i].getPlot();
            }
        }

        Plot cloned = null;
        if (null == newPlot) {
            return cloned;
        }
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
            return columnFieldCollection instanceof StructureColumnFieldCollection;
        }
        return false;
    }
}
