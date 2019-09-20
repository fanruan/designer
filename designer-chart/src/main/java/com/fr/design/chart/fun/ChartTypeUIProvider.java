package com.fr.design.chart.fun;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsConfigPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.injectable.SpecialLevel;
import com.fr.stable.fun.Level;

/**
 * Created by eason on 14/12/29.
 *
 * @since 8.0
 * 自定义 图表类型 界面接口
 */
public interface ChartTypeUIProvider extends Level {
    
    String XML_TAG = "ChartTypeUIProvider";

    String OLD_TAG = SpecialLevel.IndependentChartUIProvider.getTagName();

    int CURRENT_API_LEVEL = 3;

    /**
     * 图表 类型定义界面类型，就是属性表的第一个界面
     * 可以返回null 代表没有 图表类型切换界面
     *
     * @return 图表的类型定义界面类型
     */
    AbstractChartTypePane getPlotTypePane();

    /**
     * 图表 数据配置界面 即属性表的第二个界面
     * 可以返回null 代表没有数据配置界面
     */
    ChartDataPane getChartDataPane(AttributeChangeListener listener);

    /**
     * 图表 属性界面数组 其他样式界面数组
     * 可以返回空数组 代表没有其他样式界面
     *
     * @return 属性界面
     */
    AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener);

    /**
     * 图表 名称
     * eg:柱形图
     *
     * @return 图表 名称
     */
    String getName();

    /**
     * 图表 名称
     * 柱形图 堆积柱形图 等
     *
     * @return 图表 名称
     */
    String[] getSubName();

    /**
     * 图表 demo图片路径
     * 400*225
     * 1.图表选择界面的图的路径 原样渲染
     * 2.图表属性第一个界面 类型界面 缩放渲染
     *
     * @return demo图片路径
     */
    String[] getDemoImagePath();

    /**
     * 小图标路径
     * 表单 工具栏 图表小图标
     * 16*16
     *
     * @return 图标路径
     */
    String getIconPath();


    //todo:把下面这些接口删除
    @Deprecated
    boolean needChartChangePane();

    /**
     * 数据集数据源的界面
     *
     * @return 数据集数据源的界面
     */
    @Deprecated
    AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent);


    /**
     * 单元格数据源的界面
     *
     * @return 单元格数据源的界面
     */
    @Deprecated
    AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent);

    /**
     * 条件属性界面
     *
     * @return 条件属性界面
     */
    @Deprecated
    ConditionAttributesPane getPlotConditionPane(Plot plot);

    /**
     * 系列界面
     *
     * @return 系列界面
     */
    @Deprecated
    BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot);

    /**
     * 是否使用默认的界面，为了避免界面来回切换
     *
     * @return 是否使用默认的界面
     */
    @Deprecated
    boolean isUseDefaultPane();

    @Deprecated
    ChartEditPane getChartEditPane(String plotID);

    @Deprecated
    ChartsConfigPane getChartConfigPane(String plotID);

}