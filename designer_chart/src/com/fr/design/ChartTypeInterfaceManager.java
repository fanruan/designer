package com.fr.design;

import com.fr.base.FRContext;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
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
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsConfigPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.form.ui.ChartEditor;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.DownloadOnlineSourcesHelper;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.area.AreaIndependentVanChartInterface;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.bar.BarIndependentVanChartInterface;
import com.fr.plugin.chart.bubble.BubbleIndependentVanChartInterface;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.column.ColumnIndependentVanChartInterface;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.custom.CustomIndependentVanChartInterface;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.drillmap.DrillMapIndependentVanChartInterface;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;
import com.fr.plugin.chart.funnel.designer.FunnelIndependentVanChartInterface;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gantt.designer.GanttIndependentVanChartInterface;
import com.fr.plugin.chart.gauge.GaugeIndependentVanChartInterface;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.heatmap.designer.HeatMapIndependentVanChartInterface;
import com.fr.plugin.chart.line.LineIndependentVanChartInterface;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.plugin.chart.map.MapIndependentVanChartInterface;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.multilayer.MultiPieIndependentVanChartInterface;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlot;
import com.fr.plugin.chart.phantom.VanChartPhantomService;
import com.fr.plugin.chart.pie.PieIndependentVanChartInterface;
import com.fr.plugin.chart.radar.RadarIndependentVanChartInterface;
import com.fr.plugin.chart.radar.VanChartRadarPlot;
import com.fr.plugin.chart.scatter.ScatterIndependentVanChartInterface;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.structure.desinger.StructureIndependentVanChartInterface;
import com.fr.plugin.chart.treemap.TreeMapIndependentVanChartInterface;
import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;
import com.fr.plugin.chart.vanchart.imgevent.design.DesignImageEvent;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;
import com.fr.plugin.chart.wordcloud.designer.WordCloudIndependentVanChartInterface;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.injectable.PluginSingleInjection;
import com.fr.plugin.solution.closeable.CloseableContainedMap;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fr.chart.charttypes.ChartTypeManager.CHART_PRIORITY;

/**
 * Created by eason on 14/12/29.
 */
public class ChartTypeInterfaceManager implements ExtraChartDesignClassManagerProvider {
    
    
    private static ChartTypeInterfaceManager classManager = new ChartTypeInterfaceManager();
    
    private static LinkedHashMap<String, CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>> chartTypeInterfaces =
        new LinkedHashMap<String, CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>>();
    
    public synchronized static ChartTypeInterfaceManager getInstance() {
        
        return classManager;
    }
    
    static {
        readDefault();
        readVanChart();
        PluginModule.registerAgent(PluginModule.ExtraChartDesign, classManager);
    }
    
    static {

        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {
                //重新注册designModuleFactory
                DesignModuleFactory.registerExtraWidgetOptions(initWidgetOption());
                DesignImageEvent.registerDefaultCallbackEvent(HistoryTemplateListPane.getInstance());
                DesignImageEvent.registerDownloadSourcesEvent(new DownloadOnlineSourcesHelper());
            }
        });
    }
    
    private static WidgetOption[] initWidgetOption() {
        
        ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption[] child = new ChartWidgetOption[typeName.length];
        final Chart[][] allCharts = new Chart[typeName.length][];
        for (int i = 0; i < typeName.length; i++) {
            String plotID = typeName[i].getPlotID();
            Chart[] rowChart = ChartTypeManager.getInstance().getChartTypes(plotID);
            if (ArrayUtils.isEmpty(rowChart)) {
                continue;
            }
            String iconPath = ChartTypeInterfaceManager.getInstance().getIconPath(plotID);
            Icon icon = IOUtils.readIcon(iconPath);
            child[i] = new ChartWidgetOption(Inter.getLocText(typeName[i].getName()), icon, ChartEditor.class, rowChart[0]);
            
            allCharts[i] = rowChart;
        }
        
        //异步加载图片
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    VanChartPhantomService.startPhantomService();
                } catch (Exception e){
                    FRContext.getLogger().info("phantomjs server startup failure：" + e.getMessage());
                }
                initAllChartsDemoImage(allCharts);
            }
        }).start();
        
        return child;
    }
    
    //加载所有图表图片
    private static void initAllChartsDemoImage(Chart[][] allCharts) {
        
        for (Chart[] rowChart : allCharts) {
            if (rowChart == null) {
                continue;
            }
            //加载初始化图表模型图片
            initChartsDemoImage(rowChart);
        }
    }
    
    private static void initChartsDemoImage(Chart[] rowChart) {
        
        for (Chart aRowChart : rowChart) {
            //此时，为图片生成模型数据
            aRowChart.createSlotImage();
        }
    }

    private static void readVanChart() {

        if (chartTypeInterfaces.containsKey(ChartTypeManager.VAN_CHART_PRIORITY)) {
            return;
        }
        CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap> chartUIList =
                new CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>(LinkedHashMap.class);
        chartUIList.put(PiePlot4VanChart.VAN_CHART_PIE_PLOT, new PieIndependentVanChartInterface());
        chartUIList.put(VanChartColumnPlot.VAN_CHART_COLUMN_PLOT_ID, new ColumnIndependentVanChartInterface());
        chartUIList.put(VanChartColumnPlot.VAN_CHART_BAR_PLOT_ID, new BarIndependentVanChartInterface());
        chartUIList.put(VanChartLinePlot.VAN_CHART_LINE_PLOT, new LineIndependentVanChartInterface());
        chartUIList.put(VanChartAreaPlot.VAN_CHART_AREA_PLOT_ID, new AreaIndependentVanChartInterface());
        chartUIList.put(VanChartGaugePlot.VAN_CHART_GAUGE_PLOT, new GaugeIndependentVanChartInterface());
        chartUIList.put(VanChartRadarPlot.VAN_CHART_RADAR_PLOT, new RadarIndependentVanChartInterface());
        chartUIList.put(VanChartScatterPlot.VAN_CHART_SCATTER_PLOT_ID, new ScatterIndependentVanChartInterface());
        chartUIList.put(VanChartBubblePlot.VAN_CHART_BUBBLE_PLOT_ID, new BubbleIndependentVanChartInterface());
        chartUIList.put(VanChartCustomPlot.VAN_CHART_CUSTOM_PLOT_ID, new CustomIndependentVanChartInterface());
        chartUIList.put(VanChartMultiPiePlot.VAN_CHART_MULTILAYER_PLOT_ID, new MultiPieIndependentVanChartInterface());
        chartUIList.put(VanChartMapPlot.VAN_CHART_MAP_ID, new MapIndependentVanChartInterface());
        chartUIList.put(VanChartDrillMapPlot.VAN_CHART_DRILL_MAP_ID, new DrillMapIndependentVanChartInterface());
        chartUIList.put(VanChartTreeMapPlot.VAN_CHART_TREE_MAP_PLOT_ID, new TreeMapIndependentVanChartInterface());
        chartUIList.put(VanChartFunnelPlot.VAN_CHART_FUNNEL_PLOT_ID, new FunnelIndependentVanChartInterface());
        chartUIList.put(VanChartHeatMapPlot.VAN_CHART_HEAT_MAP_ID, new HeatMapIndependentVanChartInterface());
        chartUIList.put(VanChartWordCloudPlot.WORD_CLOUD_PLOT_ID, new WordCloudIndependentVanChartInterface());
        chartUIList.put(VanChartGanttPlot.VAN_CHART_GANTT_PLOT_ID, new GanttIndependentVanChartInterface());
        chartUIList.put(VanChartStructurePlot.STRUCTURE_PLOT_ID, new StructureIndependentVanChartInterface());

        chartTypeInterfaces.put(ChartTypeManager.VAN_CHART_PRIORITY, chartUIList);
    }
    
    
    private static void readDefault() {
        
        if (chartTypeInterfaces.containsKey(CHART_PRIORITY)) {
            return;
        }
        CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap> chartUIList =
            new CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>(LinkedHashMap.class);
        chartUIList.put(ChartConstants.COLUMN_CHART, new ColumnIndependentChartInterface());
        chartUIList.put(ChartConstants.LINE_CHART, new LineIndependentChartInterface());
        chartUIList.put(ChartConstants.BAR_CHART, new BarIndependentChartInterface());
        chartUIList.put(ChartConstants.PIE_CHART, new PieIndependentChartInterface());
        chartUIList.put(ChartConstants.AREA_CHART, new AreaIndependentChartInterface());
        chartUIList.put(ChartConstants.SCATTER_CHART, new XYScatterIndependentChartInterface());
        chartUIList.put(ChartConstants.BUBBLE_CHART, new BubbleIndependentChartInterface());
        chartUIList.put(ChartConstants.RADAR_CHART, new RadarIndependentChartInterface());
        chartUIList.put(ChartConstants.STOCK_CHART, new StockIndependentChartInterface());
        chartUIList.put(ChartConstants.METER_CHART, new MeterIndependentChartInterface());
        chartUIList.put(ChartConstants.RANGE_CHART, new RangeIndependentChartInterface());
        chartUIList.put(ChartConstants.CUSTOM_CHART, new CustomIndependentChartInterface());
        chartUIList.put(ChartConstants.GANTT_CHART, new GanttIndependentChartInterface());
        chartUIList.put(ChartConstants.DONUT_CHART, new DonutIndependentChartInterface());
        chartUIList.put(ChartConstants.MAP_CHART, new MapIndependentChartInterface());
        chartUIList.put(ChartConstants.GIS_CHAER, new GisMapIndependentChartInterface());
        chartUIList.put(ChartConstants.FUNNEL_CHART, new FunnelIndependentChartInterface());
        
        chartTypeInterfaces.put(CHART_PRIORITY, chartUIList);
    }
    
    
    public String getIconPath(String plotID) {
        
        if (chartTypeInterfaces != null) {
            for (Map.Entry<String, CloseableContainedMap<String, IndependentChartUIProvider, LinkedHashMap>> entry : chartTypeInterfaces.entrySet()) {
                String priority = entry.getKey();
                String imagePath = getIconPath(priority, plotID);
                if (StringUtils.isNotEmpty(imagePath)) {
                    return imagePath;
                }
            }
        }
        return StringUtils.EMPTY;
    }
    
    private String getIconPath(String priority, String plotID) {
        
        if (chartTypeInterfaces.get(priority) != null && chartTypeInterfaces.get(priority).get(plotID) != null) {
            return chartTypeInterfaces.get(priority).get(plotID).getIconPath();
        } else {
            return StringUtils.EMPTY;
        }
    }
    
    private static void addChartTypeInterface(IndependentChartUIProvider provider, String priority, String plotID) {
        
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
        }
    }
    
    
    /**
     * 把所有的pane加到list里
     *
     * @param paneList pane容器
     */
    public void addPlotTypePaneList(List<FurtherBasicBeanPane<? extends Chart>> paneList) {
        
        List<Integer> priorityList = getPriorityInOrder();
        for (Integer aPriorityList : priorityList) {
            String priority = String.valueOf(aPriorityList);
            Iterator chartUIIterator = chartTypeInterfaces.get(priority).entrySet().iterator();
            while (chartUIIterator.hasNext()) {
                Map.Entry chartUIEntry = (Map.Entry) chartUIIterator.next();
                IndependentChartUIProvider provider = (IndependentChartUIProvider) chartUIEntry.getValue();
                paneList.add(provider.getPlotTypePane());
            }
        }
    }
    
    public String[] getTitle4PopupWindow(String priority) {
        
        if (priority.isEmpty()) {
            return getTitle4PopupWindow();
        }
        String[] names = new String[getChartSize(priority)];
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority)) {
            Map<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(priority);
            Iterator iterator = chartUIList.entrySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                IndependentChartUIProvider provider = (IndependentChartUIProvider) entry.getValue();
                names[i++] = provider.getPlotTypeTitle4PopupWindow();
            }
            return names;
        }
        return new String[0];
    }
    
    /**
     * 获取指定图表的标题
     */
    public String getTitle4PopupWindow(String priority, String plotID) {
        
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority) && chartTypeInterfaces.get(priority).containsKey(plotID)) {
            IndependentChartUIProvider provider = chartTypeInterfaces.get(priority).get(plotID);
            return provider.getPlotTypeTitle4PopupWindow();
        }
        
        //兼容老的插件
        if (chartTypeInterfaces != null) {
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String defaultPriority = (String) entry.getKey();
                if (chartTypeInterfaces.get(defaultPriority).containsKey(plotID)) {
                    return chartTypeInterfaces.get(defaultPriority).get(plotID).getPlotTypeTitle4PopupWindow();
                }
            }
        }
        return StringUtils.EMPTY;
    }
    
    private String[] getTitle4PopupWindow() {
        
        List<Integer> priorityList = getPriorityInOrder();
        
        if (priorityList.size() == 0) {
            return new String[0];
        }
        
        int size = 0;
        //获取总得图表格式
        for (Integer aPriorityList : priorityList) {
            size += getChartSize(String.valueOf(aPriorityList));
        }
        String[] names = new String[size];
        
        int index = 0;
        for (Integer aPriorityList : priorityList) {
            String priority = String.valueOf(aPriorityList);
            Iterator chartUI = chartTypeInterfaces.get(priority).entrySet().iterator();
            index = fetchNames(chartUI, names, index);
        }
        
        return names;
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
    
    private int fetchNames(Iterator chartUI, String[] names, int index) {
        
        while (chartUI.hasNext()) {
            Map.Entry chartUIEntry = (Map.Entry) chartUI.next();
            IndependentChartUIProvider provider = (IndependentChartUIProvider) chartUIEntry.getValue();
            names[index++] = provider.getPlotTypeTitle4PopupWindow();
        }
        return index;
    }
    
    public ChartDataPane getChartDataPane(String plotID, AttributeChangeListener listener) {
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plotID, priority)) {
                return getChartDataPane(priority, plotID, listener);
            }
        }
        return getChartDataPane(CHART_PRIORITY, plotID, listener);
    }
    
    private ChartDataPane getChartDataPane(String priority, String plotID, AttributeChangeListener listener) {
        
        return chartTypeInterfaces.get(priority).get(plotID).getChartDataPane(listener);
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
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plotID, priority)) {
                return getAttrPaneArray(priority, plotID, listener);
            }
        }
        return getAttrPaneArray(CHART_PRIORITY, plotID, listener);
    }
    
    private AbstractChartAttrPane[] getAttrPaneArray(String priority, String plotID, AttributeChangeListener listener) {
        
        return chartTypeInterfaces.get(priority).get(plotID).getAttrPaneArray(listener);
    }
    
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), priority)) {
                return getTableDataSourcePane(priority, plot, parent);
            }
        }
        return getTableDataSourcePane(CHART_PRIORITY, plot, parent);
    }
    
    private AbstractTableDataContentPane getTableDataSourcePane(String priority, Plot plot, ChartDataPane parent) {
        
        return chartTypeInterfaces.get(priority).get(plot.getPlotID()).getTableDataSourcePane(plot, parent);
    }
    
    
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            String plotID = plot.getPlotID();
            if (plotInChart(plotID, priority)) {
                return getReportDataSourcePane(priority, plot, parent);
            }
        }
        return getReportDataSourcePane(CHART_PRIORITY, plot, parent);
    }
    
    private boolean plotInChart(String plotID, String priority) {
        
        return chartTypeInterfaces != null
            && chartTypeInterfaces.containsKey(priority)
            && chartTypeInterfaces.get(priority).containsKey(plotID);
    }
    
    private AbstractReportDataContentPane getReportDataSourcePane(String priority, Plot plot, ChartDataPane parent) {
        
        return chartTypeInterfaces.get(priority).get(plot.getPlotID()).getReportDataSourcePane(plot, parent);
    }
    
    
    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), priority)) {
                return getPlotConditionPane(priority, plot);
            }
        }
        return getPlotConditionPane(CHART_PRIORITY, plot);
    }
    
    private ConditionAttributesPane getPlotConditionPane(String priority, Plot plot) {
        
        return chartTypeInterfaces.get(priority).get(plot.getPlotID()).getPlotConditionPane(plot);
    }
    
    
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), priority)) {
                return getPlotSeriesPane(priority, parent, plot);
            }
        }
        return getPlotSeriesPane(CHART_PRIORITY, parent, plot);
    }
    
    private BasicBeanPane<Plot> getPlotSeriesPane(String priority, ChartStylePane parent, Plot plot) {
        
        return chartTypeInterfaces.get(priority).get(plot.getPlotID()).getPlotSeriesPane(parent, plot);
    }
    
    /**
     * 是否使用默认的界面，为了避免界面来回切换
     *
     * @param plotID 序号
     * @return 是否使用默认的界面
     */
    public boolean isUseDefaultPane(String plotID) {
        
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (chartTypeInterfaces.get(priority).containsKey(plotID)) {
                return isUseDefaultPane(priority, plotID);
            }
        }
        
        return true;
    }
    
    private boolean isUseDefaultPane(String priority, String plotID) {
        
        return !(chartTypeInterfaces.containsKey(priority) && chartTypeInterfaces.get(priority).containsKey(plotID)) || chartTypeInterfaces.get(priority).get(plotID).isUseDefaultPane();
        
    }
    
    @Override
    public void mount(PluginSingleInjection injection) {
        
        if (isIndependentChartUIProvider(injection)) {
            String priority = injection.getAttribute("priority", CHART_PRIORITY);
            String plotID = injection.getAttribute("plotID");
            IndependentChartUIProvider instance = (IndependentChartUIProvider) injection.getObject();
            addChartTypeInterface(instance, priority, plotID);
        }
    }
    
    
    @Override
    public void demount(PluginSingleInjection injection) {
        
        if (isIndependentChartUIProvider(injection)) {
            String priority = injection.getAttribute("priority", CHART_PRIORITY);
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


    //获取指定图表的编辑面板
    public ChartEditPane getChartEditPane(String plotID) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plotID, priority)) {
                return getChartEditPane(priority, plotID);
            }
        }
        return getChartEditPane(CHART_PRIORITY, plotID);
    }

    private ChartEditPane getChartEditPane(String priority, String plotID) {
        return chartTypeInterfaces.get(priority).get(plotID).getChartEditPane(plotID);
    }

    public ChartsConfigPane getChartConfigPane(String plotID) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plotID, priority)) {
                return getChartConfigPane(priority, plotID);
            }
        }
        return getChartConfigPane(CHART_PRIORITY, plotID);
    }

    private ChartsConfigPane getChartConfigPane(String priority, String plotID) {
        return chartTypeInterfaces.get(priority).get(plotID).getChartConfigPane(plotID);
    }

}