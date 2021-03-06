package com.fr.van.chart.gauge;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.MeterPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.van.chart.custom.component.CategoryCustomPlotTableDataContentPane;
import com.fr.van.chart.custom.component.MeterCustomPlotReportDataContentPane;
import com.fr.van.chart.custom.component.MeterCustomPlotTableDataContentPane;
import com.fr.van.chart.custom.component.VanChartDataPane;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 15/11/27.
 */
public class GaugeIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/gauge.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartGaugePlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;
            switch (gaugePlot.getGaugeStyle()){
                case POINTER:
                    return gaugePlot.isInCustom() ? new CategoryCustomPlotTableDataContentPane(parent) : new CategoryPlotTableDataContentPane(parent);
                case POINTER_SEMI:
                    return gaugePlot.isInCustom() ? new CategoryCustomPlotTableDataContentPane(parent) : new CategoryPlotTableDataContentPane(parent);
                default:
                    break;
            }
        }
        return ((VanChartGaugePlot)plot).isInCustom() ? new MeterCustomPlotTableDataContentPane(parent) : new MeterPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;
            switch (gaugePlot.getGaugeStyle()){
                case POINTER:
                    return new CategoryPlotReportDataContentPane(parent);
                case POINTER_SEMI:
                    return new CategoryPlotReportDataContentPane(parent);
                default:
                    break;
            }
        }
        return (parent instanceof VanChartDataPane) ? new MeterCustomPlotReportDataContentPane(parent) : new MeterPlotReportDataContentPane(parent);
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartGaugeStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane();
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartGaugeSeriesPane(parent, plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartGaugePlotPane.TITLE;
    }
}