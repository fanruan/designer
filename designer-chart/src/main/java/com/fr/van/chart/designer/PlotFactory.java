package com.fr.van.chart.designer;

import com.fr.base.chart.chartdata.model.LargeDataModel;
import com.fr.chart.chartattr.Plot;
import com.fr.data.core.FormatField;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.style.FormatPane;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlot;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;
import com.fr.van.chart.bubble.force.VanChartBubbleRefreshTooltipPane;
import com.fr.van.chart.designer.component.VanChartLabelContentPane;
import com.fr.van.chart.designer.component.VanChartRefreshTooltipContentPane;
import com.fr.van.chart.designer.component.VanChartTooltipContentPane;
import com.fr.van.chart.designer.style.HeatMapRangeLegendPane;
import com.fr.van.chart.designer.style.VanChartPlotLegendPane;
import com.fr.van.chart.designer.style.VanChartRangeLegendPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.VanLegendPaneWidthOutHighlight;
import com.fr.van.chart.designer.style.label.VanChartGaugePlotLabelPane;
import com.fr.van.chart.designer.style.label.VanChartPlotLabelPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotRefreshTooltipPane;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.van.chart.funnel.designer.style.VanChartFunnelLabelContentPane;
import com.fr.van.chart.funnel.designer.style.VanChartFunnelRefreshTooltipContentPane;
import com.fr.van.chart.funnel.designer.style.VanChartFunnelTooltipContentPane;
import com.fr.van.chart.gantt.designer.style.VanChartGanttLabelContentPane;
import com.fr.van.chart.gantt.designer.style.tooltip.VanChartGanttPlotTooltipPane;
import com.fr.van.chart.gantt.designer.style.tooltip.VanChartGanttTooltipContentPane;
import com.fr.van.chart.gauge.VanChartGaugePlotRefreshTooltipPane;
import com.fr.van.chart.gauge.VanChartGaugePlotTooltipPane;
import com.fr.van.chart.map.designer.style.label.VanChartMapLabelContentPane;
import com.fr.van.chart.map.designer.style.tooltip.VanChartMapRefreshTooltipContentPane;
import com.fr.van.chart.map.designer.style.tooltip.VanChartMapTooltipContentPane;
import com.fr.van.chart.multilayer.style.VanChartMultiPieLabelContentPane;
import com.fr.van.chart.multilayer.style.VanChartMultiPiePlotTooltipPane;
import com.fr.van.chart.multilayer.style.VanChartMultiPieTooltipContentPane;
import com.fr.van.chart.multilayer.style.VanChartMutiPieRefreshTooltipContentPane;
import com.fr.van.chart.scatter.VanChartScatterPlotTooltipPane;
import com.fr.van.chart.scatter.VanChartScatterRefreshTooltipContentPane;
import com.fr.van.chart.scatter.component.label.VanChartScatterPlotLabelPane;
import com.fr.van.chart.structure.desinger.style.VanChartStructureLabelContentPane;
import com.fr.van.chart.structure.desinger.style.VanChartStructureRefreshTooltipContentPane;
import com.fr.van.chart.structure.desinger.style.VanChartStructureTooltipContentPane;
import com.fr.van.chart.wordcloud.designer.style.VanChartWordCloudRefreshTootipContentPane;
import com.fr.van.chart.wordcloud.designer.style.VanChartWordCloudTooltipContentPane;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mitisky on 16/3/1.
 * 图表配置界面的工厂
 */
public class PlotFactory {

    private static Set<Class<? extends Plot>> autoAdjustLabelPlots = new HashSet<Class<? extends Plot>>();

    static {
        autoAdjustLabelPlots.add(VanChartColumnPlot.class);
        autoAdjustLabelPlots.add(VanChartLinePlot.class);
        autoAdjustLabelPlots.add(VanChartAreaPlot.class);
        autoAdjustLabelPlots.add(VanChartStructurePlot.class);
    }

    public static boolean plotAutoAdjustLabelPosition(Plot plot) {
        return autoAdjustLabelPlots.contains(plot.getClass());
    }

    /**
     * 标签Map
     */
    private static Map<Class<? extends Plot>, Class<? extends VanChartPlotLabelPane>> labelMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartPlotLabelPane>>();

    static {
        labelMap.put(VanChartGaugePlot.class, VanChartGaugePlotLabelPane.class);
        labelMap.put(VanChartScatterPlot.class, VanChartScatterPlotLabelPane.class);
        labelMap.put(VanChartBubblePlot.class, VanChartScatterPlotLabelPane.class);
    }

    /**
     * 图例Map
     */
    private static Map<Class<? extends Plot>, Class<? extends VanChartPlotLegendPane>> legendMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartPlotLegendPane>>();

    static {
        legendMap.put(VanChartGaugePlot.class, VanLegendPaneWidthOutHighlight.class);
        legendMap.put(VanChartMultiPiePlot.class, VanLegendPaneWidthOutHighlight.class);
        legendMap.put(VanChartScatterPlot.class, VanChartRangeLegendPane.class);
        legendMap.put(VanChartBubblePlot.class, VanChartRangeLegendPane.class);
        legendMap.put(VanChartMapPlot.class, VanChartRangeLegendPane.class);
        legendMap.put(VanChartDrillMapPlot.class, VanChartRangeLegendPane.class);
        legendMap.put(VanChartHeatMapPlot.class, HeatMapRangeLegendPane.class);
        legendMap.put(VanChartWordCloudPlot.class, VanChartRangeLegendPane.class);
    }

    /**
     * 数据点提示Map
     */
    private static Map<Class<? extends Plot>, Class<? extends VanChartPlotTooltipPane>> toolTipMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartPlotTooltipPane>>();

    static {
        toolTipMap.put(VanChartGaugePlot.class, VanChartGaugePlotTooltipPane.class);
        toolTipMap.put(VanChartScatterPlot.class, VanChartScatterPlotTooltipPane.class);
        toolTipMap.put(VanChartBubblePlot.class, VanChartScatterPlotTooltipPane.class);
        toolTipMap.put(VanChartMultiPiePlot.class, VanChartMultiPiePlotTooltipPane.class);
        toolTipMap.put(VanChartTreeMapPlot.class, VanChartMultiPiePlotTooltipPane.class);
        toolTipMap.put(VanChartGanttPlot.class, VanChartGanttPlotTooltipPane.class);
    }

    private static Map<Class<? extends Plot>, Class<? extends VanChartTooltipContentPane>> labelContentMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartTooltipContentPane>>();

    static {
        labelContentMap.put(VanChartMapPlot.class, VanChartMapLabelContentPane.class);
        labelContentMap.put(VanChartDrillMapPlot.class, VanChartMapLabelContentPane.class);
        labelContentMap.put(VanChartMultiPiePlot.class, VanChartMultiPieLabelContentPane.class);
        labelContentMap.put(VanChartTreeMapPlot.class, VanChartMultiPieLabelContentPane.class);
        labelContentMap.put(VanChartFunnelPlot.class, VanChartFunnelLabelContentPane.class);
        labelContentMap.put(VanChartHeatMapPlot.class, VanChartMapLabelContentPane.class);
        labelContentMap.put(VanChartGanttPlot.class, VanChartGanttLabelContentPane.class);
        labelContentMap.put(VanChartStructurePlot.class, VanChartStructureLabelContentPane.class);
    }

    private static Map<Class<? extends Plot>, Class<? extends VanChartTooltipContentPane>> tooltipContentMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartTooltipContentPane>>();

    static {
        tooltipContentMap.put(VanChartMapPlot.class, VanChartMapTooltipContentPane.class);
        tooltipContentMap.put(VanChartDrillMapPlot.class, VanChartMapTooltipContentPane.class);
        tooltipContentMap.put(VanChartMultiPiePlot.class, VanChartMultiPieTooltipContentPane.class);
        tooltipContentMap.put(VanChartTreeMapPlot.class, VanChartMultiPieTooltipContentPane.class);
        tooltipContentMap.put(VanChartFunnelPlot.class, VanChartFunnelTooltipContentPane.class);
        tooltipContentMap.put(VanChartHeatMapPlot.class, VanChartMapTooltipContentPane.class);
        tooltipContentMap.put(VanChartWordCloudPlot.class, VanChartWordCloudTooltipContentPane.class);
        tooltipContentMap.put(VanChartGanttPlot.class, VanChartGanttTooltipContentPane.class);
        tooltipContentMap.put(VanChartStructurePlot.class, VanChartStructureTooltipContentPane.class);
    }


    /**
     * 监控刷新 自动数据点提示Map
     */


    private static Map<Class<? extends Plot>, Class<? extends VanChartPlotTooltipPane>> refreshToolTipMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartPlotTooltipPane>>();

    static {
        refreshToolTipMap.put(VanChartGaugePlot.class, VanChartGaugePlotRefreshTooltipPane.class);
        refreshToolTipMap.put(VanChartBubblePlot.class, VanChartBubbleRefreshTooltipPane.class);

    }

    private static Map<Class<? extends Plot>, Class<? extends VanChartTooltipContentPane>> refreshTooltipContentMap = new HashMap<Class<? extends Plot>, Class<? extends VanChartTooltipContentPane>>();

    static {
        refreshTooltipContentMap.put(VanChartMapPlot.class, VanChartMapRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartHeatMapPlot.class, VanChartMapRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartDrillMapPlot.class, VanChartMapRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartScatterPlot.class, VanChartScatterRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartBubblePlot.class, VanChartScatterRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartMultiPiePlot.class, VanChartMutiPieRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartTreeMapPlot.class, VanChartMutiPieRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartFunnelPlot.class, VanChartFunnelRefreshTooltipContentPane.class);
        refreshTooltipContentMap.put(VanChartWordCloudPlot.class, VanChartWordCloudRefreshTootipContentPane.class);
        refreshTooltipContentMap.put(VanChartStructurePlot.class, VanChartStructureRefreshTooltipContentPane.class);

    }

    /**
     * 根据图表类型创建标签界面
     *
     * @param plot      图表
     * @param stylePane 样式界面
     * @return 标签界面
     */
    public static VanChartPlotLabelPane createPlotLabelPane(Plot plot, VanChartStylePane stylePane) {
        Class<? extends Plot> key = plot.getClass();
        if (labelMap.containsKey(key)) {
            try {
                Class<? extends VanChartPlotLabelPane> cl = labelMap.get(key);
                Constructor<? extends VanChartPlotLabelPane> constructor = cl.getConstructor(Plot.class, VanChartStylePane.class);
                return constructor.newInstance(plot, stylePane);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartPlotLabelPane(plot, stylePane);
    }

    /**
     * 根据图表类型创建图例界面
     *
     * @param plot      图表
     * @param stylePane 样式界面
     * @return 图例界面
     */
    public static VanChartPlotLegendPane createPlotLegendPane(Plot plot, VanChartStylePane stylePane) {
        Class<? extends Plot> key = plot.getClass();
        if (legendMap.containsKey(key)) {
            try {
                Class<? extends VanChartPlotLegendPane> cl = legendMap.get(key);
                Constructor<? extends VanChartPlotLegendPane> constructor = cl.getConstructor(VanChartStylePane.class);
                return constructor.newInstance(stylePane);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartPlotLegendPane(stylePane);
    }

    /**
     * 根据图表类型创建数据点提示界面
     *
     * @param plot      图表
     * @param stylePane 样式界面
     * @return 数据点提示界面
     */
    public static VanChartPlotTooltipPane createPlotTooltipPane(Plot plot, VanChartStylePane stylePane) {
        Class<? extends Plot> key = plot.getClass();
        if (toolTipMap.containsKey(key)) {
            try {
                Class<? extends VanChartPlotTooltipPane> cl = toolTipMap.get(key);
                Constructor<? extends VanChartPlotTooltipPane> constructor = cl.getConstructor(Plot.class, VanChartStylePane.class);
                return constructor.newInstance(plot, stylePane);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartPlotTooltipPane(plot, stylePane);
    }

    /**
     * 根据图表类型创建标签的具体内容界面.分类名系列名等
     *
     * @param plot       图表
     * @param parent     样式界面
     * @param showOnPane formatpane用到
     * @return 标签的具体内容界面
     */
    public static VanChartTooltipContentPane createPlotLabelContentPane(Plot plot, VanChartStylePane parent, JPanel showOnPane) {
        Class<? extends Plot> key = plot.getClass();
        if (labelContentMap.containsKey(key)) {
            try {
                Class<? extends VanChartTooltipContentPane> cl = labelContentMap.get(key);
                Constructor<? extends VanChartTooltipContentPane> constructor = cl.getConstructor(VanChartStylePane.class, JPanel.class);
                return constructor.newInstance(parent, showOnPane);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartLabelContentPane(parent, showOnPane);
    }

    /**
     * 根据图表类型创建数据点提示的具体内容界面.分类名系列名等
     *
     * @param plot       图表
     * @param parent     样式界面
     * @param showOnPane formatpane用到
     * @return 数据点提示的具体内容界面
     */
    public static VanChartTooltipContentPane createPlotTooltipContentPane(Plot plot, VanChartStylePane parent, JPanel showOnPane) {
        Class<? extends Plot> key = plot.getClass();
        if (tooltipContentMap.containsKey(key)) {
            try {
                Class<? extends VanChartTooltipContentPane> cl = tooltipContentMap.get(key);
                Constructor<? extends VanChartTooltipContentPane> constructor = cl.getConstructor(VanChartStylePane.class, JPanel.class);
                return constructor.newInstance(parent, showOnPane);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartTooltipContentPane(parent, showOnPane);
    }


    /**
     * 根据图表类型创建数据点提示界面
     *
     * @param plot 图表
     * @return 数据点提示界面
     */
    public static VanChartPlotTooltipPane createPlotRefreshTooltipPane(Plot plot) {
        Class<? extends Plot> key = plot.getClass();
        if (refreshToolTipMap.containsKey(key)) {
            try {
                Class<? extends VanChartPlotTooltipPane> cl = refreshToolTipMap.get(key);
                Constructor<? extends VanChartPlotTooltipPane> constructor = cl.getConstructor(Plot.class);
                return constructor.newInstance(plot);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartPlotRefreshTooltipPane(plot);
    }

    /**
     * 根据图表类型创建监控刷新中数据点提示的具体内容界面.分类名系列名等
     *
     * @param plot       图表
     * @param parent     交互属性界面
     * @param showOnPane formatpane用到
     * @return 数据点提示的具体内容界面
     */
    public static VanChartTooltipContentPane createPlotRefreshTooltipContentPane(Plot plot, VanChartStylePane parent, JPanel showOnPane) {
        Class<? extends Plot> key = plot.getClass();
        if (refreshTooltipContentMap.containsKey(key)) {
            try {
                Class<? extends VanChartTooltipContentPane> cl = refreshTooltipContentMap.get(key);
                Constructor<? extends VanChartTooltipContentPane> constructor = cl.getConstructor(VanChartStylePane.class, JPanel.class);
                return constructor.newInstance(parent, showOnPane);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return new VanChartRefreshTooltipContentPane(parent, showOnPane);
    }

    public static FormatPane createAutoFormatPane() {
        FormatPane formatPane = new FormatPane() {
            protected Component[][] getComponent(JPanel fontPane, JPanel centerPane, JPanel typePane) {
                typePane.setBorder(BorderFactory.createEmptyBorder());
                return new Component[][]{
                        new Component[]{typePane, null},
                        new Component[]{centerPane, null},
                };
            }

            protected UIComboBoxRenderer createComBoxRender() {
                return new UIComboBoxRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Integer) {
                            String text = ComparatorUtils.equals(value, FormatField.FormatContents.NULL)
                                    ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto")
                                    : FormatField.getInstance().getName((Integer) value);
                            label.setText(" " + text);
                        }
                        return label;
                    }
                };
            }
        };

        formatPane.setComboBoxModel(true);

        return formatPane;
    }

    /**
     * 判断是否为大数据模式
     */
    public static boolean largeDataModel(Plot plot) {
        return plot != null && plot.convertDataProcessor().getMark() == LargeDataModel.MARK;
    }
}

