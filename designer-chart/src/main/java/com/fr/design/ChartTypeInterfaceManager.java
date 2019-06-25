package com.fr.design;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chartx.attr.XChart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.chartinterface.AreaIndependentChartInterface;
import com.fr.design.chartinterface.BarIndependentChartInterface;
import com.fr.design.chartinterface.BubbleIndependentChartInterface;
import com.fr.design.chartinterface.ColumnIndependentChartInterface;
import com.fr.design.chartinterface.CustomIndependentChartInterface;
import com.fr.design.chartinterface.DonutIndependentChartInterface;
import com.fr.design.chartinterface.FunnelIndependentChartInterface;
import com.fr.design.chartinterface.GanttIndependentChartInterface;
import com.fr.design.chartinterface.GisMapIndependentChartInterface;
import com.fr.design.chartinterface.LineIndependentChartInterface;
import com.fr.design.chartinterface.MapIndependentChartInterface;
import com.fr.design.chartinterface.MeterIndependentChartInterface;
import com.fr.design.chartinterface.PieIndependentChartInterface;
import com.fr.design.chartinterface.RadarIndependentChartInterface;
import com.fr.design.chartinterface.RangeIndependentChartInterface;
import com.fr.design.chartinterface.StockIndependentChartInterface;
import com.fr.design.chartinterface.XYScatterIndependentChartInterface;
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
import com.fr.form.ui.ChartEditor;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.locale.InterProviderFactory;
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
    
    private static LinkedHashMap<String, CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>> chartTypeInterfaces =
        new LinkedHashMap<String, CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>>();

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

        ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption[] child = new ChartWidgetOption[typeName.length];
        int index = 0;
        for (ChartInternationalNameContentBean bean : typeName) {
            String plotID = bean.getPlotID();
            XChart[] rowChart = ChartTypeManager.getInstance().getChartTypes(plotID);
            if (ArrayUtils.isEmpty(rowChart) && !ChartTypeManager.innerChart(plotID)) {
                continue;
            }
            String iconPath = ChartTypeInterfaceManager.getInstance().getIconPath(plotID);
            Icon icon = IOUtils.readIcon(iconPath);
            child[index] = new ChartWidgetOption(InterProviderFactory.getProvider().getLocText(bean.getName()), icon, ChartEditor.class, plotID);
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

        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.COLUMN_CHART, new ColumnIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.LINE_CHART, new LineIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.BAR_CHART, new BarIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.PIE_CHART, new PieIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.AREA_CHART, new AreaIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.SCATTER_CHART, new XYScatterIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.BUBBLE_CHART, new BubbleIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.RADAR_CHART, new RadarIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.STOCK_CHART, new StockIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.METER_CHART, new MeterIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.RANGE_CHART, new RangeIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.CUSTOM_CHART, new CustomIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.GANTT_CHART, new GanttIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.DONUT_CHART, new DonutIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.MAP_CHART, new MapIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.GIS_CHAER, new GisMapIndependentChartInterface());
        addChartTypeInterface(DEPRECATED_CHART_PRIORITY, ChartConstants.FUNNEL_CHART, new FunnelIndependentChartInterface());
    }
    
    private static void addChartTypeInterface(String priority, String plotID, IndependentChartUIProvider provider) {
        
        if (chartTypeInterfaces != null) {
            if (!chartTypeInterfaces.containsKey(priority)) {
                //新建一个具体图表列表
                CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap> chartUIList
                    = new CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>(LinkedHashMap.class);
                chartUIList.put(plotID, provider);
                chartTypeInterfaces.put(priority, chartUIList);
            } else {
                Map<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(priority);
                if (!chartUIList.containsKey(plotID)) {
                    chartUIList.put(plotID, provider);
                }
            }
            idAndPriorityMap.put(plotID, priority);
        }
    }

    private IndependentChartUIProvider getChartTypeInterface(String plotID) {
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
    public void addPlotTypePaneList(List<FurtherBasicBeanPane<? extends Chart>> paneList, Map<String, Map<String, FurtherBasicBeanPane<? extends Chart>>> allChartTypePane) {
        
        List<Integer> priorityList = getPriorityInOrder();
        for (Integer aPriorityList : priorityList) {
            String priority = String.valueOf(aPriorityList);
            addPlotTypePaneList(priority, paneList, allChartTypePane);
        }
    }


    public void addPlotTypePaneList(String priority, List<FurtherBasicBeanPane<? extends Chart>> paneList, Map<String, Map<String, FurtherBasicBeanPane<? extends Chart>>> allChartTypePane) {

        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority)) {

            Map<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(priority);

            Iterator<Map.Entry<String, IndependentChartUIProvider>> iterator = chartUIList.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, IndependentChartUIProvider> entry = iterator.next();
                String plotID = entry.getKey();

                AbstractChartTypePane pane = entry.getValue().getPlotTypePane();
                if (AssistUtils.equals(pane.title4PopupWindow(), TYPE_PANE_DEFAULT_TITLE)) {
                    continue;
                }
                pane.setPlotID(plotID);
                paneList.add(pane);

                if (allChartTypePane.get(priority) == null) {
                    allChartTypePane.put(priority, new LinkedHashMap<String, FurtherBasicBeanPane<? extends Chart>>());
                }
                allChartTypePane.get(priority).put(plotID, pane);
            }
        }
    }

    private String getChartName(String plotID, IndependentChartUIProvider provider) {
        String name = provider.getPlotTypeTitle4PopupWindow();
        if (StringUtils.isEmpty(name)) {
            name = ChartTypeManager.getInstance().getChartName(plotID);
        }
        return name;
    }

    public String getTitle4PopupWindow(String plotID) {
        IndependentChartUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getPlotTypeTitle4PopupWindow();
        }
        return StringUtils.EMPTY;
    }
    
    /**
     * 获取指定图表的标题
     */
    public String getTitle4PopupWindow(String priority, String plotID) {
        
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority) && chartTypeInterfaces.get(priority).containsKey(plotID)) {
            IndependentChartUIProvider provider = chartTypeInterfaces.get(priority).get(plotID);
            return getChartName(plotID, provider);
        }
        
        //兼容老的插件
        if (chartTypeInterfaces != null) {
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String defaultPriority = (String) entry.getKey();
                if (chartTypeInterfaces.get(defaultPriority).containsKey(plotID)) {
                    return getChartName(plotID, chartTypeInterfaces.get(defaultPriority).get(plotID));
                }
            }
        }
        return StringUtils.EMPTY;
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
            IndependentChartUIProvider provider = getChartTypeInterface(plotID);
            if (provider != null) {
                return provider.getIconPath();
            }
        }
        return StringUtils.EMPTY;
    }
    
    public ChartDataPane getChartDataPane(String plotID, AttributeChangeListener listener) {
        IndependentChartUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getChartDataPane(listener);
        }

        return new ChartDataPane(listener);
    }
    
    /**
     * 获取对应ID的图表数量
     *
     */
    private int getChartSize(String key) {
        
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(key)) {
            return chartTypeInterfaces.get(key).size();
        }
        return 0;
    }
    
    public AbstractChartAttrPane[] getAttrPaneArray(String plotID, AttributeChangeListener listener) {
        IndependentChartUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getAttrPaneArray(listener);
        }

        return new AbstractChartAttrPane[0];
        
    }
    
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {

        IndependentChartUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getTableDataSourcePane(plot, parent);
        }

        return null;
    }
    
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {

        IndependentChartUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getReportDataSourcePane(plot, parent);
        }

        return null;
    }
    
    private boolean plotInChart(String plotID, String priority) {
        
        return chartTypeInterfaces != null
            && chartTypeInterfaces.containsKey(priority)
            && chartTypeInterfaces.get(priority).containsKey(plotID);
    }
    
    
    public ConditionAttributesPane getPlotConditionPane(Plot plot) {

        IndependentChartUIProvider provider = getChartTypeInterface(plot.getPlotID());
        if (provider != null) {
            return provider.getPlotConditionPane(plot);
        }

        return null;
    }
    
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {

        IndependentChartUIProvider provider = getChartTypeInterface(plot.getPlotID());
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

        IndependentChartUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.isUseDefaultPane();
        }
        
        return true;
    }

    public boolean needChartChangePane(Chart chart) {
        if (chart != null && chart.getPlot() != null) {
            IndependentChartUIProvider provider = getChartTypeInterface(chart.getPlot().getPlotID());
            if (provider != null) {
                return provider.needChartChangePane();
            }
        }

        return true;
    }
    
    @Override
    public void mount(PluginSingleInjection injection) {
        
        if (isIndependentChartUIProvider(injection)) {
            String priority = injection.getAttribute("priority", DEFAULT_PRIORITY);
            String plotID = injection.getAttribute("plotID");
            IndependentChartUIProvider instance = (IndependentChartUIProvider) injection.getObject();
            addChartTypeInterface(priority, plotID, instance);
        }
    }
    
    
    @Override
    public void demount(PluginSingleInjection injection) {
        
        if (isIndependentChartUIProvider(injection)) {
            String priority = injection.getAttribute("priority", DEFAULT_PRIORITY);
            String plotID = injection.getAttribute("plotID");
            removeChartTypeInterface(priority, plotID);
        }
    }
    
    private void removeChartTypeInterface(String priority, String plotID) {
        
        if (chartTypeInterfaces != null) {
            if (chartTypeInterfaces.containsKey(priority)) {
                Map<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(priority);
                chartUIList.remove(plotID);
            }
        }
    }
    
    
    private boolean isIndependentChartUIProvider(PluginSingleInjection injection) {
        
        return !(injection == null || injection.getObject() == null) && IndependentChartUIProvider.XML_TAG.equals(injection.getName()) && injection.getObject() instanceof IndependentChartUIProvider;
    }


    public ChartEditPane getChartEditPane(String plotID) {
        IndependentChartUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getChartEditPane(plotID);
        }
        return new ChartEditPane();
    }

    public ChartsConfigPane getChartConfigPane(String plotID) {
        IndependentChartUIProvider provider = getChartTypeInterface(plotID);
        if (provider != null) {
            return provider.getChartConfigPane(plotID);
        }

        return null;
    }

}