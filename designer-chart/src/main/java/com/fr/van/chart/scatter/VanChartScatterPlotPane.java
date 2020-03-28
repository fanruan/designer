package com.fr.van.chart.scatter;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.field.AbstractColumnFieldCollection;
import com.fr.chartx.data.field.diff.BubbleColumnFieldCollection;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.scatter.ScatterIndependentVanChart;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 16/2/16.
 */
public class VanChartScatterPlotPane extends AbstractVanChartTypePane {
    private static final long serialVersionUID = -3481633368542654247L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/scatter/images/scatter.png"
        };
    }

    protected Plot getSelectedClonedPlot(){
        VanChartScatterPlot newPlot = null;
        Chart[] scatterChart = ScatterIndependentVanChart.ScatterVanChartTypes;
        for(int i = 0, len = scatterChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartScatterPlot)scatterChart[i].getPlot();
            }
        }
        Plot cloned = null;
        if (null == newPlot) {
            return cloned;
        }
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In ScatterChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return ScatterIndependentVanChart.ScatterVanChartTypes[0];
    }

    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        return tools;
    }

    @Override
    /**
     *删除配置的条件属性
     */
    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        cloneOldDefaultAttrConditionCollection(oldPlot, newPlot);
    }

    @Override
    /**
     * 删除线型配置
     */
    protected void cloneOldDefaultAttrConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            ConditionCollection newCondition = new ConditionCollection();
            newCondition.setDefaultAttr((ConditionAttr) oldPlot.getConditionCollection().getDefaultAttr().clone());
            newPlot.setConditionCollection(newCondition);

            //删除线型设置
            ConditionAttr attrList = newCondition.getDefaultAttr();
            DataSeriesCondition attr = attrList.getExisted(VanChartAttrLine.class);
            if (attr != null){
                attrList.remove(VanChartAttrLine.class);
            }
        }
    }

    @Override
    protected boolean acceptDefinition(ChartDataDefinitionProvider definition, VanChartPlot vanChartPlot) {
        if(definition instanceof AbstractDataDefinition) {
            AbstractColumnFieldCollection columnFieldCollection = ((AbstractDataDefinition) definition).getColumnFieldCollection();
            return columnFieldCollection instanceof BubbleColumnFieldCollection;
        }
        return false;
    }
}