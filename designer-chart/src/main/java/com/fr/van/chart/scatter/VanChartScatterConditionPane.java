package com.fr.van.chart.scatter;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.plugin.chart.scatter.attr.ScatterAttrLabel;
import com.fr.plugin.chart.scatter.attr.ScatterAttrTooltip;
import com.fr.van.chart.bubble.VanChartBubbleConditionSelectionPane;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartMarkerConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTrendLineConditionPane;
import com.fr.van.chart.scatter.component.VanChartScatterLineTypeConditionPane;
import com.fr.van.chart.scatter.component.label.VanChartScatterLabelConditionPane;
import com.fr.van.chart.scatter.component.tooltip.VanChartScatterTooltipConditionPane;
import com.fr.van.chart.scatter.large.VanChartLargeModelMarkerConditionPane;

import java.awt.Dimension;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartScatterConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    public VanChartScatterConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }
    @Override
    protected ChartConditionPane createListConditionPane() {
        return new VanChartBubbleConditionSelectionPane();
    }
    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(ScatterAttrTooltip.class, new VanChartScatterTooltipConditionPane(this, plot));
        classPaneMap.put(VanChartAttrTrendLine.class, new VanChartTrendLineConditionPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        if(PlotFactory.largeDataModel(plot)){
            classPaneMap.put(VanChartAttrMarker.class, new VanChartLargeModelMarkerConditionPane(this));
        } else {
            classPaneMap.put(VanChartAttrLine.class, new VanChartScatterLineTypeConditionPane(this));
            classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
            classPaneMap.put(ScatterAttrLabel.class, new VanChartScatterLabelConditionPane(this, plot));
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getScatterPlotDefaultEffect()));
        }
    }

    protected void addStyleAction() {

    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartScatterPlot.class;
    }
}