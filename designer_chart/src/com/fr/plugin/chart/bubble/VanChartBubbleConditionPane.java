package com.fr.plugin.chart.bubble;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.bubble.attr.VanChartAttrBubble;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.other.condition.item.VanChartBubbleSetConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.plugin.chart.scatter.attr.ScatterAttrLabel;
import com.fr.plugin.chart.scatter.attr.ScatterAttrTooltip;
import com.fr.plugin.chart.scatter.component.label.VanChartScatterLabelConditionPane;
import com.fr.plugin.chart.scatter.component.tooltip.VanChartScatterTooltipConditionPane;

import java.awt.*;

/**
 * Created by Mitisky on 16/3/31.
 */
public class VanChartBubbleConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    public VanChartBubbleConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    private boolean forceBubble() {
        return plot instanceof VanChartBubblePlot && ((VanChartBubblePlot) plot).isForceBubble();
    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return forceBubble() ? new ChartConditionPane() : new VanChartBubbleConditionSelectionPane();
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(VanChartAttrBubble.class, new VanChartBubbleSetConditionPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));

        if(forceBubble()){
            addForceAction();
        } else if(PlotFactory.largeDataModel(plot)){
            addLargeAction();
        } else {
            addNormalAction();
        }
    }

    private void addNormalAction() {
        classPaneMap.put(ScatterAttrLabel.class, new VanChartScatterLabelConditionPane(this, plot));
        classPaneMap.put(ScatterAttrTooltip.class, new VanChartScatterTooltipConditionPane(this, plot));
        classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getBubblePlotDefaultEffect()));
    }

    private void addForceAction() {
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getBubblePlotDefaultEffect()));
    }

    private void addLargeAction() {
        classPaneMap.put(ScatterAttrTooltip.class, new VanChartScatterTooltipConditionPane(this, plot));
    }


    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartBubblePlot.class;
    }
}
