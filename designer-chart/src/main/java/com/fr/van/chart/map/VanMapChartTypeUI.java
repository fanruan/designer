package com.fr.van.chart.map;

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
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.map.designer.data.VanChartMapDataPane;
import com.fr.van.chart.map.designer.data.contentpane.report.VanAreaMapPlotReportDataContentPane;
import com.fr.van.chart.map.designer.data.contentpane.report.VanLineMapPlotReportDataContentPane;
import com.fr.van.chart.map.designer.data.contentpane.report.VanPointMapPlotReportDataContentPane;
import com.fr.van.chart.map.designer.data.contentpane.table.VanAreaMapPlotTableDataContentPane;
import com.fr.van.chart.map.designer.data.contentpane.table.VanLineMapPlotTableDataContentPane;
import com.fr.van.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;
import com.fr.van.chart.map.designer.other.VanChartMapOtherPane;
import com.fr.van.chart.map.designer.other.condition.pane.VanChartMapConditionPane;
import com.fr.van.chart.map.designer.style.VanChartMapStylePane;
import com.fr.van.chart.map.designer.type.VanChartMapPlotPane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 16/5/4.
 */
public class VanMapChartTypeUI extends AbstractIndependentVanChartUI {

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Map");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Region_Map"),
                Toolkit.i18nText("Fine-Design_Chart_PointMap"),
                Toolkit.i18nText("Fine-Design_Chart_LineMap"),
                Toolkit.i18nText("Fine-Design_Chart_Combine_Map")
        };
    }


    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/33.png",
                "com/fr/plugin/chart/demo/image/34.png",
                "com/fr/plugin/chart/demo/image/35.png",
                "com/fr/plugin/chart/demo/image/36.png"
        };
    }


    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartMapPlotPane();
    }

//图表数据结构 恢复用注释。取消注释。
//    public ChartDataPane getChartDataPane(AttributeChangeListener listener){
//        return new MapChartDataPane(listener);
//    }

    //图表数据结构 恢复用注释。删除下面5个方法 getTableDataSourcePane getReportDataSourcePane getChartDataPane areaPlot linePlot。
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return areaPlot(plot) ? new VanAreaMapPlotTableDataContentPane(parent)
                : linePlot(plot) ? new VanLineMapPlotTableDataContentPane(parent)
                : new VanPointMapPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return areaPlot(plot) ? new VanAreaMapPlotReportDataContentPane(parent)
                : linePlot(plot) ? new VanLineMapPlotReportDataContentPane(parent)
                : new VanPointMapPlotReportDataContentPane(parent);
    }

    public ChartDataPane getChartDataPane(AttributeChangeListener listener){
        return new VanChartMapDataPane(listener);
    }

    protected boolean areaPlot(Plot plot) {
        return plot != null && plot instanceof VanChartMapPlot && ((VanChartMapPlot) plot).getMapType() == MapType.AREA;
    }

    protected boolean linePlot(Plot plot) {
        return plot != null && plot instanceof VanChartMapPlot && ((VanChartMapPlot) plot).getMapType() == MapType.LINE;
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartMapStylePane(listener);
        VanChartOtherPane otherPane = new VanChartMapOtherPane();
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/map.png";
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartMapSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartMapConditionPane(plot);
    }
}
