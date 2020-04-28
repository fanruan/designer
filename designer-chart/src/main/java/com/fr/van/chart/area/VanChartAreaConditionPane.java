package com.fr.van.chart.area;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.base.AttrDataSheet;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.type.ConditionKeyType;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.other.condition.item.VanChartAreaFillColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartDataSheetContentPane;
import com.fr.van.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLineTypeConditionPaneWithoutDashed;
import com.fr.van.chart.designer.other.condition.item.VanChartMarkerConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTrendLineConditionPane;
import com.fr.van.chart.scatter.large.VanChartLargeModelMarkerConditionPane;

import java.awt.Dimension;

/**
 * Created by Mitisky on 15/11/18.
 */
public class VanChartAreaConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    public VanChartAreaConditionPane(Plot plot) {
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
        classPaneMap.put(VanChartAttrTrendLine.class, new VanChartTrendLineConditionPane(this));
        classPaneMap.put(AttrAreaSeriesFillColorBackground.class, new VanChartAreaFillColorConditionPane(this, plot));
        classPaneMap.put(VanChartAttrLine.class, new VanChartLineTypeConditionPaneWithoutDashed(this));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        //是否使用数据表
        if (plot.getDataSheet().isVisible()) {
            classPaneMap.put(AttrDataSheet.class, new VanChartDataSheetContentPane(this, plot));
        }
        if(PlotFactory.largeDataModel(plot)){
            classPaneMap.put(VanChartAttrMarker.class, new VanChartLargeModelMarkerConditionPane(this));
        } else {
            classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getAreaPlotDefaultEffect()));
            classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        }
    }

    protected void addStyleAction() {

    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new ChartConditionPane(){
            @Override
            protected ConditionKeyType[] conditionKeyTypes() {
                return ConditionKeyType.CATEGORY_ARRAY_CONDITION_KEY_TYPES;
            }
        };
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartAreaPlot.class;
    }
}