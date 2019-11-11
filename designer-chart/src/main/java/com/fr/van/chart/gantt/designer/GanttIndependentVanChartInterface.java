package com.fr.van.chart.gantt.designer;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.gantt.designer.data.VanChartGanttDataPane;
import com.fr.van.chart.gantt.designer.data.data.GanttPlotReportDataContentPane;
import com.fr.van.chart.gantt.designer.data.data.GanttPlotTableDataContentPane;
import com.fr.van.chart.gantt.designer.other.VanChartGanttConditionPane;
import com.fr.van.chart.gantt.designer.other.VanChartGanttInteractivePane;
import com.fr.van.chart.gantt.designer.style.VanChartGanttStylePane;
import com.fr.van.chart.gantt.designer.style.series.VanChartGanttSeriesPane;
import com.fr.van.chart.gantt.designer.type.VanChartGanttPlotPane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by hufan on 2017/1/9.
 */
public class GanttIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Gantt");
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/44.png"
        };
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartGanttPlotPane();
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new VanChartGanttDataPane(listener);
    }

    @Override
    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartGanttConditionPane(plot);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new GanttPlotReportDataContentPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new GanttPlotTableDataContentPane();
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartGanttSeriesPane(parent, plot);
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/gantt.png";
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartGanttStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane(){
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartGanttInteractivePane();
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }
}