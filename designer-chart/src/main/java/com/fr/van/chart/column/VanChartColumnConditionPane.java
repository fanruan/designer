package com.fr.van.chart.column;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAlphaPane;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.base.AttrDataSheet;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.glyph.VanChartMultiCategoryDataPoint;
import com.fr.van.chart.designer.other.condition.item.VanChartDataSheetContentPane;
import com.fr.van.chart.designer.other.condition.item.VanChartEffectConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartFloatColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartLabelConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesColorConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartSeriesImageBackgroundConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTooltipConditionPane;
import com.fr.van.chart.designer.other.condition.item.VanChartTrendLineConditionPane;

import java.awt.Dimension;

/**
 * Created by Mitisky on 15/9/28.
 */
public class VanChartColumnConditionPane extends DataSeriesConditionPane{
    private static final long serialVersionUID = -7180705321732069806L;

    public VanChartColumnConditionPane(Plot plot) {
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
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        classPaneMap.put(AttrBorder.class, new VanChartColumnLabelBorderPane(this));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        classPaneMap.put(VanChartAttrTrendLine.class, new VanChartTrendLineConditionPane(this));
        classPaneMap.put(AttrSeriesImageBackground.class, new VanChartSeriesImageBackgroundConditionPane(this));
        classPaneMap.put(AttrEffect.class, new VanChartEffectConditionPane(this, EffectHelper.getColumnPlotDefaultEffect()));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        //是否使用数据表
        //自定义柱形图设置多X坐标轴时，不支持数据表
        if (plot.getDataSheet().isVisible() && ((VanChartColumnPlot) plot).getXAxisList().size() == 1) {
            classPaneMap.put(AttrDataSheet.class, new VanChartDataSheetContentPane(this, plot));
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
        return VanChartColumnPlot.class;
    }
}