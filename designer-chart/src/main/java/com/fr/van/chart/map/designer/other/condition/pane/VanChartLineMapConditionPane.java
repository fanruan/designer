package com.fr.van.chart.map.designer.other.condition.pane;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.line.condition.AttrCurve;
import com.fr.plugin.chart.map.line.condition.AttrLineEffect;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.other.condition.item.VanChartCurveConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartFloatColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLineEffectConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLineMapTooltipConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.van.chart.map.designer.other.condition.VanChartLineMapConditionSelectionPane;

import java.awt.Dimension;

/**
 * Created by hufan on 2016/12/23.
 */
public class VanChartLineMapConditionPane extends DataSeriesConditionPane {

    public VanChartLineMapConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new VanChartLineMapConditionSelectionPane();
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        classPaneMap.put(AttrCurve.class, new VanChartCurveConditionPane(this));
        classPaneMap.put(AttrTooltip.class, new VanChartLineMapTooltipConditionPane(this, plot));
        if (!PlotFactory.largeDataModel(plot)) {
            classPaneMap.put(AttrLineEffect.class, new VanChartLineEffectConditionPane(this, EffectHelper.getLineMapDefaultLineEffect()));
        }
    }

    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     *
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartMapPlot.class;
    }

}
