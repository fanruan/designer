package com.fr.van.chart.treemap;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.treemap.TreeMapIndependentVanChart;
import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;
import com.fr.van.chart.multilayer.VanChartMultiPiePlotPane;

/**
 * Created by Fangjie on 2016/7/11.
 */
public class VanChartTreeMapPlotPane extends VanChartMultiPiePlotPane {

    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/treemap/images/treeMap.png"
        };
    }

    @Override
    protected String getPlotTypeID() {
        return VanChartTreeMapPlot.VAN_CHART_TREE_MAP_PLOT_ID;
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_New_TreeMap");
    }

    protected Plot getSelectedClonedPlot(){
        VanChartTreeMapPlot newPlot = null;
        Chart[] treeMapVanChartTypes = TreeMapIndependentVanChart.TreeMapVanChartTypes;
        for(int i = 0, len = treeMapVanChartTypes.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartTreeMapPlot) treeMapVanChartTypes[i].getPlot();
            }
        }

        Plot cloned = null;
        if (null == newPlot) {
            return cloned;
        }
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In treeMapChart");
        }
        return cloned;
    }


    /**
     * 防止新建其他图表从而切换很卡
     * @return
     */
    public Chart getDefaultChart() {
        return TreeMapIndependentVanChart.TreeMapVanChartTypes[0];
    }
}
