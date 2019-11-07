package com.fr.design;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chart.fun.ChartTypeProvider;
import com.fr.chartx.attr.ChartProvider;
import com.fr.common.annotations.Compatible;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.fun.ChartTypeUIProvider;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.type.ui.AreaChartTypeUI;
import com.fr.design.type.ui.BarChartTypeUI;
import com.fr.design.type.ui.BubbleChartTypeUI;
import com.fr.design.type.ui.ColumnChartTypeUI;
import com.fr.design.type.ui.CustomChartTypeUI;
import com.fr.design.type.ui.DonutChartTypeUI;
import com.fr.design.type.ui.FunnelChartTypeUI;
import com.fr.design.type.ui.GanttChartTypeUI;
import com.fr.design.type.ui.GisMapChartTypeUI;
import com.fr.design.type.ui.LineChartTypeUI;
import com.fr.design.type.ui.MapChartTypeUI;
import com.fr.design.type.ui.MeterChartTypeUI;
import com.fr.design.type.ui.PieChartTypeUI;
import com.fr.design.type.ui.RadarChartTypeUI;
import com.fr.design.type.ui.RangeChartTypeUI;
import com.fr.design.type.ui.StockChartTypeUI;
import com.fr.design.type.ui.XYScatterChartTypeUI;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsConfigPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.extended.chart.AbstractChart;
import com.fr.form.ui.ChartEditor;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.invoke.Reflect;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlot;
import com.fr.plugin.chart.radar.VanChartRadarPlot;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.injectable.PluginSingleInjection;
import com.fr.plugin.injectable.SpecialLevel;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.plugin.solution.closeable.CloseableContainedMap;
import com.fr.stable.ArrayUtils;
import com.fr.stable.AssistUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.van.chart.area.AreaIndependentVanChartInterface;
import com.fr.van.chart.bar.BarIndependentVanChartInterface;
import com.fr.van.chart.bubble.BubbleIndependentVanChartInterface;
import com.fr.van.chart.column.ColumnIndependentVanChartInterface;
import com.fr.van.chart.custom.CustomIndependentVanChartInterface;
import com.fr.van.chart.drillmap.DrillMapIndependentVanChartInterface;
import com.fr.van.chart.funnel.designer.FunnelIndependentVanChartInterface;
import com.fr.van.chart.gantt.designer.GanttIndependentVanChartInterface;
import com.fr.van.chart.gauge.GaugeIndependentVanChartInterface;
import com.fr.van.chart.heatmap.designer.HeatMapIndependentVanChartInterface;
import com.fr.van.chart.line.LineIndependentVanChartInterface;
import com.fr.van.chart.map.MapIndependentVanChartInterface;
import com.fr.van.chart.multilayer.MultiPieIndependentVanChartInterface;
import com.fr.van.chart.pie.PieIndependentVanChartInterface;
import com.fr.van.chart.radar.RadarIndependentVanChartInterface;
import com.fr.van.chart.scatter.ScatterIndependentVanChartInterface;
import com.fr.van.chart.structure.desinger.StructureIndependentVanChartInterface;
import com.fr.van.chart.treemap.TreeMapIndependentVanChartInterface;
import com.fr.van.chart.wordcloud.designer.WordCloudIndependentVanChartInterface;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fr.chart.charttypes.ChartTypeManager.DEFAULT_PRIORITY;
import static com.fr.chart.charttypes.ChartTypeManager.DEPRECATED_CHART_PRIORITY;
import static com.fr.chart.charttypes.ChartTypeManager.VAN_CHART_PRIORITY;

/**
 * Created by eason on 14/12/29.
 */
public class ChartTypeInterfaceManager implements ExtraChartDesignClassManagerProvider {


    private static ChartTypeInterfaceManager classManager = new ChartTypeInterfaceManager();

    private static LinkedHashMap<String, CloseableContainedMap<String, ChartTypeUIProvider, LinkedHashMap>> chartTypeInterfaces =
            new LinkedHashMap<String, CloseableContainedMap<String, ChartTypeUIProvider, LinkedHashMap>>();

    private static Map<String, String> idAndPriorityMap = new HashMap<String, String>();

    public static final String TYPE_PANE_DEFAULT_TITLE = "DEFAULT_NAME";

    public synchronized static ChartTypeInterfaceManager getInstance() {

        return classManager;
    }

    static {
        readDefault();
        readVanChart();
        PluginModule.registerAgent(PluginModule.ExtraChartDesign, classManager);

    }

    //安装插件，图表类型热更新。
    //不在static原因：放在static，启动过程中图表插件init也会触发（不需要）
    public static void addPluginChangedListener() {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {

                synchronized (ChartTypeInterfaceManager.class) {
                    //因为是CloseableContainedMap，所以不能在mount那边处理。
                    DesignModuleFactory.registerExtraWidgetOptions(ChartTypeInterfaceManager.initWidgetOption());
                }
            }
        }, new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {

                return context.contain(PluginModule.ExtraChartDesign, SpecialLevel.IndependentChartUIProvider.getTagName());
            }
        });
    }

    public static WidgetOption[] initWidgetOption() {

        String[] chartIDs = ChartTypeManager.getInstance().getAllChartIDs();
        ChartWidgetOption[] child = new ChartWidgetOption[chartIDs.length];
        int index = 0;
        for (String chartID : chartIDs) {
            ChartProvider[] rowChart = ChartTypeManager.getInstance().getCharts(chartID);
            if (ArrayUtils.isEmpty(rowChart) && !ChartTypeManager.innerChart(chartID)) {
                continue;
            }
            String name = ChartTypeInterfaceManager.getInstance().getName(chartID);
            String iconPath = ChartTypeInterfaceManager.getInstance().getIconPath(chartID);
            Icon icon = IOUtils.readIcon(iconPath);
            child[index] = new ChartWidgetOption(name, icon, ChartEditor.class, chartID);
            index++;
        }

        return child;
    }

    private static void readVanChart() {

        addChartTypeInterface(VAN_CHART_PRIORITY, PiePlot4VanChart.VAN_CHART_PIE_PLOT, new PieIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartColumnPlot.VAN_CHART_COLUMN_PLOT_ID, new ColumnIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartColumnPlot.VAN_CHART_BAR_PLOT_ID, new BarIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartLinePlot.VAN_CHART_LINE_PLOT, new LineIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartAreaPlot.VAN_CHART_AREA_PLOT_ID, new AreaIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartGaugePlot.VAN_CHART_GAUGE_PLOT, new GaugeIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartRadarPlot.VAN_CHART_RADAR_PLOT, new RadarIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartScatterPlot.VAN_CHART_SCATTER_PLOT_ID, new ScatterIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartBubblePlot.VAN_CHART_BUBBLE_PLOT_ID, new BubbleIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartCustomPlot.VAN_CHART_CUSTOM_PLOT_ID, new CustomIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartMultiPiePlot.VAN_CHART_MULTILAYER_PLOT_ID, new MultiPieIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartMapPlot.VAN_CHART_MAP_ID, new MapIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartDrillMapPlot.VAN_CHART_DRILL_MAP_ID, new DrillMapIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartTreeMapPlot.VAN_CHART_TREE_MAP_PLOT_ID, new TreeMapIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartFunnelPlot.VAN_CHART_FUNNEL_PLOT_ID, new FunnelIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartHeatMapPlot.VAN_CHART_HEAT_MAP_ID, new HeatMapIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartWordCloudPlot.WORD_CLOUD_PLOT_ID, new WordCloudIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartGanttPlot.VAN_CHART_GANTT_PLOT_ID, new GanttIndependentVanChartInterface());
        addChartTypeInterface(VAN_CHART_PRIORITY, VanChartStructurePlot.STRUCTURE_PLOT_ID, new StructureIndependentVanChartInterface());
    }


    private static void readDefault() {

        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.COLUMN_CHART, new ColumnChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.LINE_CHART, new LineChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.BAR_CHART, new BarChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.PIE_CHART, new PieChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.AREA_CHART, new AreaChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.SCATTER_CHART, new XYScatterChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.BUBBLE_CHART, new BubbleChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.RADAR_CHART, new RadarChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.STOCK_CHART, new StockChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.METER_CHART, new MeterChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.RANGE_CHART, new RangeChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.CUSTOM_CHART, new CustomChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.GANTT_CHART, new GanttChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.DONUT_CHART, new DonutChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.MAP_CHART, new MapChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.GIS_CHAER, new GisMapChartTypeUI());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.FUNNEL_CHART, new FunnelChartTypeUI());
    }

    private static void addChartTypeInterface(String priority, String plotID, ChartTypeUIProvider provider) {

        if (chartTypeInterfaces != null) {
            if (!chartTypeInterfaces.containsKey(priority)) {
                //新建一个具体图表列表
                CloseableContainedMap<String, ChartTypeUIProvider, LinkedHashMap> chartUIList
                        = new CloseableContainedMap<String, ChartTypeUIProvider, LinkedHashMap>(LinkedHashMap.class);
                chartUIList.put(plotID, provider);
                chartTypeInterfaces.put(priority, chartUIList);
            } else {
                Map<String, ChartTypeUIProvider> chartUIList = chartTypeInterfaces.get(priority);
                if (!chartUIList.containsKey(plotID)) {
                    chartUIList.put(plotID, provider);
                }
            }
            idAndPriorityMap.put(plotID, priority);
        }
    }

    private ChartTypeUIProvider getChartTypeInterface(String plotID) {
        if (idAndPriorityMap.containsKey(plotID)) {
            String priority = idAndPriorityMap.get(plotID);
            if (chartTypeInterfaces.containsKey(priority)) {
                return chartTypeInterfaces.get(priority).get(plotID);
            }
        }
        return null;
    }

    /**
     * 把所有的pane加到list里
     *
     * @param paneList pane容器
     */
    public void addPlotTypePaneList(List<FurtherBasicBeanPane<? extends ChartProvider>> paneList, Map<String, Map<String, FurtherBasicBeanPane<? extends ChartProvider>>> allChartTypePane) {

        List<Integer> priorityList = getPriorityInOrder();
        for (Integer aPriorityList : priorityList) {
            String priority = String.valueOf(aPriorityList);
            addPlotTypePaneList(priority, paneList, allChartTypePane);
        }
    }


    public void addPlotTypePaneList(String priority, List<FurtherBasicBeanPane<? extends ChartProvider>> paneList, Map<String, Map<String, FurtherBasicBeanPane<? extends ChartProvider>>> allChartTypePane) {

        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority)) {

            Map<String, ChartTypeUIProvider> chartUIList = chartTypeInterfaces.get(priority);

            Iterator<Map.Entry<String, ChartTypeUIProvider>> iterator = chartUIList.entrySet().iterator();
            while (iterator.hasNext()) {
                try {
                    Map.Entry<String, ChartTypeUIProvider> entry = iterator.next();
                    String plotID = entry.getKey();

                    AbstractChartTypePane pane = entry.getValue().getPlotTypePane();
                    if (AssistUtils.equals(pane.title4PopupWindow(), TYPE_PANE_DEFAULT_TITLE)) {
                        continue;
                    }
                    pane.reLayout(plotID);
                    paneList.add(pane);

                    if (allChartTypePane.get(priority) == null) {
                        allChartTypePane.put(priority, new LinkedHashMap<String, FurtherBasicBeanPane<? extends ChartProvider>>());
                    }
                    allChartTypePane.get(priority).put(plotID, pane);
                } catch (Throwable e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        }
    }

    @Compatible
    public String getTitle4PopupWindow(String plotID) {
        return getName(plotID);
    }

    private List<Integer> getPriorityInOrder() {

        List<Integer> priorityList = new ArrayList<Integer>();
        if (chartTypeInterfaces != null) {
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String priority = (String) entry.getKey();
                priorityList.add(Integer.valueOf(priority));
            }
        }
        return ChartTypeManager.orderInPriority(priorityList);
    }

    public String getIconPath(String plotID) {

        if (chartTypeInterfaces != null) {
            ChartTypeUIProvider provider = getChartTypeInterface(plotID);
            if (provider != null) {
                return provider.getIconPath();
            }
        }
        return StringUtils.EMPTY;
    }

    public String[] getDemoImagePath(String chartID) {

        if (chartTypeInterfaces != null) {
            ChartTypeUIProvider provider = getChartTypeInterface(chartID);
            if (provider != null) {
                String[] result = null;
                try {
                    result = provider.getDemoImagePath();
                } catch (Throwable e) {
                    //do nothing
                }
                return ArrayUtils.isEmpty(result) ? getCompatibleDemoImagePath(chartID) : result;
            }
        }

        return new String[0];
    }

    private String[] getCompatibleDemoImagePath(String chartID) {
        String[] result = null;
        try {
            //AbstractIndependentChartsProvider
            ChartTypeProvider chartTypeProvider = Reflect.on(ChartTypeManager.getInstanceWithCheck()).call("getChartType", chartID).get();
            result = new String[]{
                    Reflect.on(chartTypeProvider).call("getChartImagePath").get()
            };

            if (ArrayUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            //do nothing
        }

        try {
            //兼容 图表类型选择界面会调到这边
            ChartProvider[] charts = ChartTypeManager.getInstanceWithCheck().getCharts(chartID);
            result = new String[charts.length];
            for (int i = 0; i < charts.length; i++) {
                //Chart && AbstractChart
                ChartProvider chart = charts[i];
                if (!(chart instanceof AbstractChart)) {//扩展图表
                    chart = Reflect.on(chart).field("subChart").get();
                }
                result[i] = Reflect.on(chart).call("demoImagePath").get();
            }

            if (ArrayUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            //do nothing
        }

        return new String[]{getIconPath(chartID)};
    }

    public String[] getSubName(String chartID) {
        if (chartTypeInterfaces != null) {
            ChartTypeUIProvider provider = getChartTypeInterface(chartID);
            if (provider != null) {
                String[] subNames = null;
                try {
                    subNames = provider.getSubName();
                } catch (Throwable throwable) {
                    //do nothing
                }
                return ArrayUtils.isEmpty(subNames) ? getCompatibleSubName(chartID, provider) : subNames;
            }
        }
        return new String[0];
    }

    //兼容
    private String[] getCompatibleSubName(String chartID, ChartTypeUIProvider provider) {
        ChartProvider[] chartProviders = ChartTypeManager.getInstanceWithCheck().getCharts(chartID);

        if (chartProviders.length == 1) {
            return new String[]{getName(chartID)};
        }
        String[] result = new String[chartProviders.length];
        for (int i = 0; i < chartProviders.length; i++) {
            if (chartProviders[i] instanceof Chart) {
                //Chart && AbstractChart
                result[i] = ((Chart) chartProviders[i]).getChartName();
            }
        }
        return result;
    }

    public String getName(String chartID) {
        if (chartTypeInterfaces != null) {
            ChartTypeUIProvider provider = getChartTypeInterface(chartID);
            if (provider != null) {
                String name = null;
                try {
                    name = provider.getName();
                } catch (Throwable throwable) {
                    //do nothing
                }

                return StringUtils.isEmpty(name) ? getCompatibleName(chartID, provider) : name;
            }
        }
        return StringUtils.EMPTY;
    }

    //兼容
    private static String getCompatibleName(String chartID, ChartTypeUIProvider provider) {

        String result = null;
        try {
            //AbstractIndependentChartsProvider
            ChartTypeProvider chartTypeProvider = Reflect.on(ChartTypeManager.getInstanceWithCheck()).call("getChartType", chartID).get();
            result = Reflect.on(chartTypeProvider).call("getChartName").get();//国际化的key
            result = InterProviderFactory.getProvider().getLocText(result);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            //do nothing
        }


        try {
            ChartProvider chartProvider = ChartTypeManager.getInstanceWithCheck().getCharts(chartID)[0];
            if (chartProvider instanceof Chart) {
                //AbstractExtendedChartUIProvider
                result = ((Chart) chartProvider).getChartName();
            }
            if (StringUtils.isNotEmpty(result) && !"Charts".equals(result)) {
                return result;
            }

        } catch (Exception e) {
            //do nothing
        }

        return provider.getPlotTypePane().title4PopupWindow();
    }

    public ChartDataPane getChartDataPane(String plotID, AttributeChangeListener listener) {
        ChartTypeUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getChartDataPane(listener);
        }

        return new ChartDataPane(listener);
    }

    public AbstractChartAttrPane[] getAttrPaneArray(String plotID, AttributeChangeListener listener) {
        ChartTypeUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getAttrPaneArray(listener);
        }

        return new AbstractChartAttrPane[0];

    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {

        ChartTypeUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getTableDataSourcePane(plot, parent);
        }

        return null;
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {

        ChartTypeUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getReportDataSourcePane(plot, parent);
        }

        return null;
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {

        ChartTypeUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getPlotConditionPane(plot);
        }

        return null;
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {

        ChartTypeUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getPlotSeriesPane(parent, plot);
        }

        return null;
    }

    /**
     * 是否使用默认的界面，为了避免界面来回切换
     *
     * @param plotID 序号
     * @return 是否使用默认的界面
     */
    public boolean isUseDefaultPane(String plotID) {

        ChartTypeUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.isUseDefaultPane();
        }

        return true;
    }

    public boolean needChartChangePane(ChartProvider chart) {
        if (chart != null) {
            String chartID = chart.getID();
            ChartTypeUIProvider provider = getChartTypeInterface(chartID);
            if (provider != null) {
                return provider.needChartChangePane();
            }
        }

        return true;
    }

    @Override
    public void mount(PluginSingleInjection injection) {

        if (isChartTypeUIProvider(injection)) {
            String id = injection.getAttribute("chartID");
            if (StringUtils.isEmpty(id)) {
                id = injection.getAttribute("plotID");
            }
            String priority = injection.getAttribute("priority", DEFAULT_PRIORITY);
            ChartTypeUIProvider instance = (ChartTypeUIProvider) injection.getObject();
            addChartTypeInterface(priority, id, instance);
        }
    }


    @Override
    public void demount(PluginSingleInjection injection) {

        if (isChartTypeUIProvider(injection)) {
            String priority = injection.getAttribute("priority", DEFAULT_PRIORITY);
            String id = injection.getAttribute("chartID");
            if (StringUtils.isEmpty(id)) {
                id = injection.getAttribute("plotID");
            }
            removeChartTypeInterface(priority, id);
        }
    }

    private void removeChartTypeInterface(String priority, String plotID) {

        if (chartTypeInterfaces != null) {
            if (chartTypeInterfaces.containsKey(priority)) {
                Map<String, ChartTypeUIProvider> chartUIList = chartTypeInterfaces.get(priority);
                chartUIList.remove(plotID);
            }
        }
    }


    private boolean isChartTypeUIProvider(PluginSingleInjection injection) {

        return !(injection == null || injection.getObject() == null) && (ChartTypeUIProvider.XML_TAG.equals(injection.getName()) || ChartTypeUIProvider.OLD_TAG.equals(injection.getName())) && injection.getObject() instanceof ChartTypeUIProvider;
    }


    public ChartEditPane getChartEditPane(String plotID) {
        ChartTypeUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getChartEditPane(plotID);
        }
        return new ChartEditPane();
    }

    public ChartsConfigPane getChartConfigPane(String plotID) {
        ChartTypeUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getChartConfigPane(plotID);
        }

        return null;
    }

}