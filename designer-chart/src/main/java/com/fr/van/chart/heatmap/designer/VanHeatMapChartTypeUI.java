package com.fr.van.chart.heatmap.designer;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.AbstractVanSingleDataPane;
import com.fr.design.chartx.fields.diff.PointMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.PointMapDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithMapZoom;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.heatmap.designer.other.VanChartHeatMapConditionPane;
import com.fr.van.chart.heatmap.designer.style.VanChartHeatMapSeriesPane;
import com.fr.van.chart.heatmap.designer.type.VanChartHeatMapTypePane;
import com.fr.van.chart.map.VanMapChartTypeUI;
import com.fr.van.chart.map.designer.style.VanChartMapStylePane;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanHeatMapChartTypeUI extends VanMapChartTypeUI {

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/heatmap.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_HeatMap");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_New_HeatMap"),
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/42.png"
        };
    }

    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartHeatMapTypePane();
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new AbstractVanSingleDataPane(listener) {
            @Override
            protected SingleDataPane createSingleDataPane() {
                PointMapDataSetFieldsPane pointMapDataSetFieldsPane = new PointMapDataSetFieldsPane();
                pointMapDataSetFieldsPane.setChart(getVanChart());
                return new SingleDataPane(pointMapDataSetFieldsPane, new PointMapCellDataFieldsPane());
            }
        };
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return new VanChartHeatMapSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return new VanChartHeatMapConditionPane(plot);
    }

    /**
     * 图表的属性界面数组
     *
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener) {
        VanChartStylePane stylePane = new VanChartMapStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane() {
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePaneWithMapZoom() {
                    @Override
                    protected boolean isCurrentChartSupportLargeDataMode() {
                        return true;
                    }
                };
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }
}