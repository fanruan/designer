package com.fr.design.chart.fun;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.stable.fun.Level;

/**
 * Created by eason on 14/12/29.
 *
 * @since 8.0
 * 自定义图表类型设设计界面接口
 */
public interface IndependentChartUIProvider extends Level {

    String XML_TAG = "IndependentChartUIProvider";

    int CURRENT_API_LEVEL = 2;

    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    AbstractChartTypePane getPlotTypePane();

    /**
     * 数据集数据源的界面
     *
     * @return 数据集数据源的界面
     */
    AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent);


    /**
     * 单元格数据源的界面
     *
     * @return 单元格数据源的界面
     */
    AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent);

    /**
     * 条件属性界面
     *
     * @return 条件属性界面
     */
    ConditionAttributesPane getPlotConditionPane(Plot plot);

    /**
     * 系列界面
     *
     * @return 系列界面
     */
    BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot);


    /**
     * 图表的属性界面数组
     *
     * @return 属性界面
     */
    AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener);

    /**
     * 是否使用默认的界面，为了避免界面来回切换
     *
     * @return 是否使用默认的界面
     */
    boolean isUseDefaultPane();

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    String getIconPath();


}