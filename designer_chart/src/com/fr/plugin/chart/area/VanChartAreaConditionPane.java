package com.fr.plugin.chart.area;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.attr.AttrDataSheet;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.attr.EffectHelper;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.other.condition.item.*;
import com.fr.plugin.chart.glyph.VanChartMultiCategoryDataPoint;
import com.fr.plugin.chart.scatter.large.VanChartLargeModelMarkerConditionPane;

import java.awt.*;

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
        classPaneMap.put(VanChartAttrLine.class, new VanChartLineTypeConditionPane(this));
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
        return VanChartAreaPlot.class;
    }
}