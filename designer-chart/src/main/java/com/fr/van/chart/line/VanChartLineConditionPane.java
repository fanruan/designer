package com.fr.van.chart.line;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrDataSheet;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.glyph.VanChartMultiCategoryDataPoint;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.other.condition.item.VanChartDataSheetContentPane;
import com.fr.van.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLineTypeConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartMarkerConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTrendLineConditionPane;
import com.fr.van.chart.scatter.large.VanChartLargeModelMarkerConditionPane;

import java.awt.Dimension;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLineConditionPane extends DataSeriesConditionPane {
    private static final long serialVersionUID = -7180705321732069806L;

    public VanChartLineConditionPane(Plot plot) {
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
        classPaneMap.put(VanChartAttrLine.class, new VanChartLineTypeConditionPane(this));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        //是否使用数据表
        if (plot.getDataSheet().isVisible()) {
            classPaneMap.put(AttrDataSheet.class, new VanChartDataSheetContentPane(this, plot));
        }
        if(PlotFactory.largeDataModel(plot)) {
            classPaneMap.put(VanChartAttrMarker.class, new VanChartLargeModelMarkerConditionPane(this));
        } else {
            classPaneMap.put(VanChartAttrMarker.class, new VanChartMarkerConditionPane(this));
            classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
            classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getLinePlotDefaultEffect()));
        }

    }

    protected void addStyleAction() {

    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new ChartConditionPane(){
            @Override
            public String[] columns2Populate() {
                return new String[]{
                        ChartConstants.CATEGORY_INDEX,
                        ChartConstants.CATEGORY_NAME,
                        ChartConstants.SERIES_INDEX,
                        ChartConstants.SERIES_NAME,
                        ChartConstants.VALUE,
                        VanChartMultiCategoryDataPoint.CATEGORY_ARRAY,
                };
            }
        };
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartLinePlot.class;
    }
}