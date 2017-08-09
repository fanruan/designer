package com.fr.plugin.chart.funnel.designer.other;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.other.condition.item.*;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;

import java.awt.*;

/**
 * Created by Mitisky on 16/10/10.
 */
public class VanChartFunnelConditionPane extends DataSeriesConditionPane {

    public VanChartFunnelConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new ChartConditionPane(){
            @Override
            public String[] columns2Populate() {
                return new String[]{
                        ChartConstants.SERIES_INDEX,
                        ChartConstants.SERIES_NAME,
                        ChartConstants.VALUE
                };
            }
        };
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getFunnelPlotDefaultEffect()));
    }

    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartFunnelPlot.class;
    }
}
