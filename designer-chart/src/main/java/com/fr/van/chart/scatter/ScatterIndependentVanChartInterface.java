package com.fr.van.chart.scatter;


import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.field.diff.BubbleColumnFieldCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.AbstractVanSingleDataPane;
import com.fr.design.chartx.fields.diff.ScatterCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.ScatterDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.BubblePlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.other.zoom.ZoomPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.scatter.component.VanChartScatterStylePane;
import com.fr.van.chart.scatter.data.VanChartScatterPlotTableDataContentPane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 16/2/16.
 */
public class ScatterIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartScatterPlotPane();
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Scatter");
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/25.png"
        };
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/scatter.png";
    }

    @Override
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return new VanChartScatterSeriesPane(parent, plot);
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new VanChartScatterPlotTableDataContentPane(parent);
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new BubblePlotReportDataContentPane(parent);
    }

    @Override
    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener) {
        VanChartStylePane stylePane = new VanChartScatterStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane() {
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePaneWithOutSort() {
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

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return new VanChartScatterConditionPane(plot);
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new AbstractVanSingleDataPane(listener) {

            @Override
            protected void populate(AbstractDataDefinition dataDefinition) {
                if (dataDefinition != null && !(dataDefinition.getColumnFieldCollection() instanceof BubbleColumnFieldCollection)) {
                    dataDefinition.setColumnFieldCollection(new BubbleColumnFieldCollection());
                }
                super.populate(dataDefinition);
            }

            @Override
            protected SingleDataPane createSingleDataPane() {
                return new SingleDataPane(new ScatterDataSetFieldsPane(), new ScatterCellDataFieldsPane());
            }
        };
    }
}