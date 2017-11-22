package com.fr.plugin.chart.structure.desinger.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;
import com.fr.plugin.chart.structure.StructureIndependentVanChart;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.wordcloud.WordCloudIndependentVanChart;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureTypePane extends AbstractVanChartTypePane{
    @Override
    protected String[] getTypeIconPath() {
        return new String[]{
                "/com/fr/plugin/chart/structure/images/vertical.png",
                "/com/fr/plugin/chart/structure/images/horizontal.png",
                "/com/fr/plugin/chart/structure/images/radial.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_Vertical_Structure"),
                Inter.getLocText("Plugin-ChartF_Horizontal_Structure"),
                Inter.getLocText("Plugin-ChartF_Radial_Structure")
        };
    }

    @Override
    protected String getPlotTypeID() {
        return VanChartStructurePlot.STRUCTURE_PLOT_ID;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewStructure");
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
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
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

}
