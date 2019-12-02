package com.fr.van.chart.vanchart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.AbstractVanSingleDataPane;
import com.fr.design.chartx.fields.diff.MultiCategoryCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.MultiCategoryDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.type.AxisType;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.data.VanChartMoreCateReportDataContentPane;
import com.fr.van.chart.designer.data.VanChartMoreCateTableDataContentPane;
import com.fr.van.chart.designer.other.VanChartInteractivePane;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.other.zoom.ZoomPane;
import com.fr.van.chart.designer.style.VanChartStylePane;

/**
 * Created by mengao on 2017/7/6.
 */
public abstract class AbstractMultiCategoryVanChartUI extends AbstractIndependentVanChartUI {
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        //自定义组合图特殊处理
        if (((VanChartPlot) plot).isInCustom() && ((VanChartPlot) plot).getCustomType().equals("CUSTOM")) {
            return new CategoryPlotTableDataContentPane(parent);
        }
        return new VanChartMoreCateTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        //自定义组合图特殊处理
        if (((VanChartPlot) plot).isInCustom() && ((VanChartPlot) plot).getCustomType().equals("CUSTOM")) {
            return new CategoryPlotReportDataContentPane(parent);
        }
        return new VanChartMoreCateReportDataContentPane(parent);
    }

    @Override
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener) {

        VanChartStylePane stylePane = new VanChartStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane() {
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePane() {
                    @Override
                    protected ZoomPane createZoomPane() {
                        return new ZoomPane();
                    }

                    @Override
                    protected boolean isCurrentChartSupportLargeDataMode() {
                        return true;
                    }
                };
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new AbstractVanSingleDataPane(listener) {
            MultiCategoryDataSetFieldsPane multiCategoryDataSetFieldsPane;
            MultiCategoryCellDataFieldsPane multiCategoryCellDataFieldsPane;

            @Override
            protected SingleDataPane createSingleDataPane() {
                multiCategoryDataSetFieldsPane = new MultiCategoryDataSetFieldsPane();
                multiCategoryCellDataFieldsPane = new MultiCategoryCellDataFieldsPane();
                return new SingleDataPane(multiCategoryDataSetFieldsPane, multiCategoryCellDataFieldsPane);
            }

            @Override
            public void populate(ChartCollection collection) {
                super.populate(collection);
                if (collection == null) {
                    return;
                }
                VanChart vanChart = collection.getSelectedChartProvider(VanChart.class);
                if (vanChart == null) {
                    return;
                }

                VanChartRectanglePlot plot = vanChart.getPlot();
                VanChartAxis axis = plot.getDefaultXAxis();
                if (plot instanceof VanChartColumnPlot
                        && ((VanChartColumnPlot) plot).isBar()) {
                    axis = plot.getDefaultYAxis();
                }

                multiCategoryDataSetFieldsPane.setCategoryAxis(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_CATEGORY));
                multiCategoryCellDataFieldsPane.setCategoryAxis(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_CATEGORY));
            }
        };
    }
}
