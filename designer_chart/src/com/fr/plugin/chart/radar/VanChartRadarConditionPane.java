package com.fr.plugin.chart.radar;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.other.condition.item.*;

import java.awt.*;

/**
 * Created by Mitisky on 15/12/29.
 */
public class VanChartRadarConditionPane extends DataSeriesConditionPane{
    private static final long serialVersionUID = 5696786807262339122L;

    public VanChartRadarConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        if(plot instanceof VanChartRadarPlot && ((VanChartRadarPlot)plot).isStackChart()) {
            classPaneMap.put(AttrBorder.class, new LabelBorderPane(this));
            classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getStackRadarPlotDefaultEffect()));
        } else {
            classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
            classPaneMap.put(VanChartAttrLine.class, new VanChartLineWidthConditionPane(this));
            classPaneMap.put(AttrAreaSeriesFillColorBackground.class, new VanChartAreaFillColorConditionPane(this));
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getRadarPlotDefaultEffect()));
        }
    }

    protected void addStyleAction() {

    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartRadarPlot.class;
    }
}