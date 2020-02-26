package com.fr.van.chart.funnel.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.field.AbstractColumnFieldCollection;
import com.fr.chartx.data.field.diff.ColumnFieldCollectionWithSeriesValue;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.funnel.FunnelIndependentVanChart;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 16/10/10.
 */
public class VanChartFunnelTypePane extends AbstractVanChartTypePane {

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{
                "/com/fr/van/chart/funnel/images/funnel.png"
        };
    }

    protected Plot getSelectedClonedPlot(){
        Chart chart = getDefaultChart();
        VanChartFunnelPlot newPlot = (VanChartFunnelPlot) chart.getPlot();

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
        return FunnelIndependentVanChart.FunnelVanChartTypes[0];
    }

    @Override
    protected boolean acceptDefinition(ChartDataDefinitionProvider definition, VanChartPlot vanChartPlot) {
        if (definition instanceof AbstractDataDefinition) {
            AbstractColumnFieldCollection columnFieldCollection = ((AbstractDataDefinition) definition).getColumnFieldCollection();
            return columnFieldCollection.getClass().equals(ColumnFieldCollectionWithSeriesValue.class);
        }
        return false;
    }
}
