package com.fr.plugin.chart.map.designer.other.condition.pane;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.other.condition.item.*;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.designer.other.condition.VanChartMapConditionSelectionPane;
import com.fr.plugin.chart.map.designer.other.condition.item.VanChartBorderWithAlphaConditionPane;

import java.awt.*;

/**
 * Created by Mitisky on 16/5/4.
 */
public class VanChartMapConditionPane extends DataSeriesConditionPane {

    public VanChartMapConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new VanChartMapConditionSelectionPane();
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        addDiffAction();
    }

    protected void addDiffAction() {
        classPaneMap.put(AttrBorderWithAlpha.class, new VanChartBorderWithAlphaConditionPane(this));
        classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getMapPlotDefaultEffect()));
        addLabelAction();
    }

    protected void addLabelAction() {
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
    }

    protected boolean addLabelOrEffectAction() {
        return !PlotFactory.largeDataModel(plot);
    }

    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartMapPlot.class;
    }
}
