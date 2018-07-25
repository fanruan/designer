package com.fr.plugin.chart.structure.desinger.other;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrNode;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.other.condition.item.VanChartFloatColorConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartStructureNodeConditionPane;
import com.fr.plugin.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.type.ConditionKeyType;

import java.awt.Dimension;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureConditionPane extends DataSeriesConditionPane {

    public VanChartStructureConditionPane(Plot plot) {
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
            protected ConditionKeyType[] conditionKeyTypes() {
                return ConditionKeyType.STRUCTURE_CONDITION_KEY_TYPES;
            }
        };
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        classPaneMap.put(AttrNode.class, new VanChartStructureNodeConditionPane(this));
    }

    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartStructurePlot.class;
    }
}

