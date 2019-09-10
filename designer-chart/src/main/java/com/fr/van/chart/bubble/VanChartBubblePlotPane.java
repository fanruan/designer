package com.fr.van.chart.bubble;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.base.VanChartZoom;
import com.fr.plugin.chart.bubble.BubbleIndependentVanChart;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.scatter.attr.ScatterAttrLabel;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubblePlotPane extends AbstractVanChartTypePane {

    private static final long serialVersionUID = -3481633368542654247L;

    private static final float FORCE_ALPHA = 1.0f;

    private static final float ALPHA = 0.7f;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/bubble/images/bubble.png",
                "/com/fr/van/chart/bubble/images/force.png"
        };
    }

    private void removeDefaultAttr(ConditionAttr conditionAttr, Class <? extends DataSeriesCondition> targetClass) {
        DataSeriesCondition attr = conditionAttr.getExisted(targetClass);
        if (attr != null){
            conditionAttr.remove(targetClass);
        }
    }

    protected Plot getSelectedClonedPlot(){
        VanChartBubblePlot newPlot = null;
        Chart[] bubbleChart = BubbleIndependentVanChart.BubbleVanChartTypes;
        for(int i = 0, len = bubbleChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartBubblePlot)bubbleChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
             if(newPlot != null) {
                 cloned = (Plot) newPlot.clone();
             }
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In BubbleChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return BubbleIndependentVanChart.BubbleVanChartTypes[0];
    }

    @Override
     /**
      * 力學氣泡圖切換到其他氣泡圖時，刪除條件屬性
      * 并且将bubbleAttr属性重置
      */
     protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        cloneOldDefaultAttrConditionCollection(oldPlot, newPlot);
    }

    @Override
    protected void cloneOldDefaultAttrConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            ConditionCollection newCondition = new ConditionCollection();
            newCondition.setDefaultAttr((ConditionAttr) oldPlot.getConditionCollection().getDefaultAttr().clone());
            newPlot.setConditionCollection(newCondition);

            ConditionAttr attrList = newCondition.getDefaultAttr();

            //根据气泡图类型，重设透明度属性
            removeDefaultAttr(attrList, AttrAlpha.class);

            //删除标签属性（防止切换到大数据气泡图标签属性会拷贝过去）
            removeDefaultAttr(attrList, ScatterAttrLabel.class);

            AttrAlpha attrAlpha = new AttrAlpha();
            attrAlpha.setAlpha(((VanChartBubblePlot)newPlot).isForceBubble() ? FORCE_ALPHA : ALPHA);

            attrList.addDataSeriesCondition(attrAlpha);
        }
    }

    @Override
    protected void cloneHotHyperLink(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException {
        if(oldPlot instanceof VanChartBubblePlot && newPlot instanceof VanChartBubblePlot){
            if(((VanChartBubblePlot) oldPlot).isForceBubble() == ((VanChartBubblePlot) newPlot).isForceBubble()){
                super.cloneHotHyperLink(oldPlot, newPlot);
            }
        }
    }

    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        return tools;
    }

    /**
     * 气泡图相同图表类型之间切换的时候，chart的部分属性也需要重置
     * @param chart
     */
    @Override
    protected void resetChartAttr4SamePlot(Chart chart){
        VanChartZoom vanChartZoom = new VanChartZoom();
        ((VanChart)chart).setVanChartZoom(vanChartZoom);
        //重置监控刷新选项
        resetRefreshMoreLabelAttr((VanChart)chart);
    }
}
