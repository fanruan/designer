package com.fr.design;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.chartinterface.*;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.file.XMLFileManager;
import com.fr.form.ui.ChartEditor;
import com.fr.general.*;
import com.fr.plugin.PluginCollector;
import com.fr.plugin.PluginLicenseManager;
import com.fr.plugin.PluginMessage;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.Authorize;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.stable.plugin.PluginReadListener;
import com.fr.stable.plugin.PluginSimplify;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import javax.swing.*;
import java.util.*;

/**
 * Created by eason on 14/12/29.
 */
public class ChartTypeInterfaceManager extends XMLFileManager implements ExtraChartDesignClassManagerProvider {




    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

    private static ChartTypeInterfaceManager classManager = null;
    private static LinkedHashMap<String, LinkedHashMap<String, IndependentChartUIProvider>> chartTypeInterfaces = new LinkedHashMap<String, LinkedHashMap<String, IndependentChartUIProvider>>();

    public synchronized static ChartTypeInterfaceManager getInstance() {
        if (classManager == null) {
            classManager = new ChartTypeInterfaceManager();
            chartTypeInterfaces.clear();
            classManager.readDefault();
        }
        return classManager;
    }

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            public void envChanged() {
                ChartTypeInterfaceManager.envChanged();
            }
        });
    }

    static {
        GeneralContext.addPluginReadListener(new PluginReadListener() {
            @Override
            public void success() {
                //重新注册designModuleFactory
                DesignModuleFactory.registerExtraWidgetOptions(initWidgetOption());
            }
        });
    }

    public static WidgetOption[] initWidgetOption(){

        ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption[] child = new ChartWidgetOption[typeName.length];
        final Chart[][] allCharts = new Chart[typeName.length][];
        for (int i = 0; i < typeName.length; i++) {
            String plotID = typeName[i].getPlotID();
            Chart[] rowChart = ChartTypeManager.getInstance().getChartTypes(plotID);
            if(rowChart == null) {
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
                initAllChartsDemoImage(allCharts);
            }
        }).start();

        return child;
    }

    //加载所有图表图片
    private static void initAllChartsDemoImage(Chart[][] allCharts){
        for (int i = 0; i < allCharts.length; i++) {
            Chart[] rowChart = allCharts[i];
            if(rowChart == null) {
                continue;
            }
            //加载初始化图表模型图片
            initChartsDemoImage(rowChart);
        }
    }

    private static void initChartsDemoImage(Chart[] rowChart) {
        int rowChartsCount = rowChart.length;
        for (int j = 0; j < rowChartsCount; j++) {
            //此时，为图片生成模型数据
            rowChart[j].createSlotImage();
        }
    }

    private synchronized static void envChanged() {
        classManager = null;
    }

    private static void readDefault() {
        if (chartTypeInterfaces.containsKey(ChartTypeManager.CHART_PRIORITY)){
            return;
        }
        LinkedHashMap<String, IndependentChartUIProvider> chartUIList = new LinkedHashMap<String, IndependentChartUIProvider>();
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

        chartTypeInterfaces.put(ChartTypeManager.CHART_PRIORITY, chartUIList);
    }

    public String getIconPath(String plotID) {
        if (chartTypeInterfaces != null) {
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String priority = (String) entry.getKey();
                String imagePath = getIconPath(priority, plotID);
                if (!StringUtils.isEmpty(imagePath)) {
                    return imagePath;
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private String getIconPath(String priority, String plotID) {
        if (chartTypeInterfaces.get(priority) != null && chartTypeInterfaces.get(priority).get(plotID) != null) {
            return chartTypeInterfaces.get(priority).get(plotID).getIconPath();
        }else {
            return StringUtils.EMPTY;
        }
    }

    public static void addChartTypeInterface(IndependentChartUIProvider provider, String priority, String plotID) {
        if (chartTypeInterfaces != null){
            if (!chartTypeInterfaces.containsKey(priority)){
                //新建一个具体图表列表
                LinkedHashMap<String, IndependentChartUIProvider> chartUIList = new LinkedHashMap<String, IndependentChartUIProvider>();
                chartUIList.put(plotID, provider);
                chartTypeInterfaces.put(priority, chartUIList);
            }else {
                LinkedHashMap<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(priority);
                if (!chartUIList.containsKey(plotID)) {
                    chartUIList.put(plotID, provider);
                }
            }
        }
    }

    /**
     * 增加界面接口定义
     *
     * @param className 类名
     * @param plotID    标志ID
     */
    public void addChartInterface(String className, String priority, String plotID, PluginSimplify simplify) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                Authorize authorize = clazz.getAnnotation(Authorize.class);
                if (authorize != null) {
                    PluginLicenseManager.getInstance().registerPaid(authorize, simplify);
                }
                IndependentChartUIProvider provider = (IndependentChartUIProvider) clazz.newInstance();
                if (PluginCollector.getCollector().isError(provider, IndependentChartUIProvider.CURRENT_API_LEVEL, simplify.getPluginName()) || !containsChart(plotID)) {
                    PluginMessage.remindUpdate(className);
                } else {
                    ChartTypeInterfaceManager.getInstance().addChartTypeInterface(provider, priority, plotID);
                }
            } catch (ClassNotFoundException e) {
                FRLogger.getLogger().error("class not found:" + e.getMessage());
            } catch (IllegalAccessException | InstantiationException e) {
                FRLogger.getLogger().error("object create error:" + e.getMessage());
            }
        }
    }

    //UI对应的chart如果没有加载,UI也不必加进去了
    private boolean containsChart(String plotID) {
        return ChartTypeManager.getInstance().containsPlot(plotID);
    }


    /**
     * 把所有的pane加到list里
     *
     * @param paneList pane容器
     */
    public void addPlotTypePaneList(List<FurtherBasicBeanPane<? extends Chart>> paneList) {
        List<Integer> priorityList = getPriorityInOrder();
        for (int i = 0; i < priorityList.size(); i++){
            String priority = String.valueOf(priorityList.get(i));
            Iterator chartUIIterator = chartTypeInterfaces.get(priority).entrySet().iterator();
            while (chartUIIterator.hasNext()) {
                Map.Entry chartUIEntry = (Map.Entry) chartUIIterator.next();
                IndependentChartUIProvider provider = (IndependentChartUIProvider) chartUIEntry.getValue();
                paneList.add(provider.getPlotTypePane());
            }
        }
    }

    public String[] getTitle4PopupWindow(String priority){
        if (priority.isEmpty()){
            return getTitle4PopupWindow();
        }
        String[] names = new String[getChartSize(priority)];
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority)){
            HashMap<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(priority);
            Iterator iterator = chartUIList.entrySet().iterator();
            int i = 0;
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                IndependentChartUIProvider provider = (IndependentChartUIProvider) entry.getValue();
                names[i++] = provider.getPlotTypePane().title4PopupWindow();
            }
            return names;
        }
        return new String[0];
    }

    /**
     * 获取指定图表的标题
     * @param priority
     * @return
     */
    public String getTitle4PopupWindow(String priority, String plotID){
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(priority) && chartTypeInterfaces.get(priority).containsKey(plotID)){
            IndependentChartUIProvider provider = chartTypeInterfaces.get(priority).get(plotID);
            return provider.getPlotTypePane().title4PopupWindow();
        }

        //兼容老的插件
        if (chartTypeInterfaces != null) {
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String defaultPriority = (String) entry.getKey();
                if (chartTypeInterfaces.get(defaultPriority).containsKey(plotID)) {
                    return chartTypeInterfaces.get(defaultPriority).get(plotID).getPlotTypePane().title4PopupWindow();
                }
            }
        }
        return new String();
    }

    private String[] getTitle4PopupWindow(){
        List<Integer> priorityList = getPriorityInOrder();

        if (priorityList.size() == 0){
            return new String[0];
        }

        int size = 0;
        //获取总得图表格式
        for (int i = 0; i < priorityList.size(); i++) {
            size += getChartSize(String.valueOf(priorityList.get(i)));
        }
        String[] names = new String[size];

        int index = 0;
        for (int i = 0; i < priorityList.size(); i++){
            String priority = String.valueOf(priorityList.get(i));
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
            names[index++] = provider.getPlotTypePane().title4PopupWindow();
        }
        return index;
    }

    public ChartDataPane getChartDataPane(String plotID, AttributeChangeListener listener) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plotID, priority)) {
                return getChartDataPane(priority, plotID, listener);
            }
        }
        return getChartDataPane(ChartTypeManager.CHART_PRIORITY, plotID, listener);
    }

    private ChartDataPane getChartDataPane(String priority, String plotID, AttributeChangeListener listener) {
        return chartTypeInterfaces.get(priority).get(plotID).getChartDataPane(listener);
    }

    /**
     * 获取对应ID的图表数量
     * @param key
     * @return
     */
    private int getChartSize(String key){
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(key)){
            return chartTypeInterfaces.get(key).size();
        }
        return 0;
    }

    public AbstractChartAttrPane[] getAttrPaneArray(String plotID, AttributeChangeListener listener) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plotID, priority)) {
                return getAttrPaneArray(priority, plotID, listener);
            }
        }
        return getAttrPaneArray(ChartTypeManager.CHART_PRIORITY, plotID, listener);
    }

    private AbstractChartAttrPane[] getAttrPaneArray(String priority, String plotID, AttributeChangeListener listener) {
        return chartTypeInterfaces.get(priority).get(plotID).getAttrPaneArray(listener);
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), priority)) {
                return getTableDataSourcePane(priority, plot, parent);
            }
        }
        return getTableDataSourcePane(ChartTypeManager.CHART_PRIORITY, plot, parent);
    }

    private AbstractTableDataContentPane getTableDataSourcePane(String priority, Plot plot, ChartDataPane parent) {
        return chartTypeInterfaces.get(priority).get(plot.getPlotID()).getTableDataSourcePane(plot, parent);
    }


    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            String plotID = plot.getPlotID();
            if (plotInChart(plotID, priority)) {
                return getReportDataSourcePane(priority, plot, parent);
            }
        }
        return getReportDataSourcePane(ChartTypeManager.CHART_PRIORITY, plot, parent);
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
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), priority)) {
                return getPlotConditionPane(priority, plot);
            }
        }
        return getPlotConditionPane(ChartTypeManager.CHART_PRIORITY, plot);
    }

    private ConditionAttributesPane getPlotConditionPane(String priority, Plot plot) {
        return chartTypeInterfaces.get(priority).get(plot.getPlotID()).getPlotConditionPane(plot);
    }


    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), priority)) {
                return getPlotSeriesPane(priority, parent, plot);
            }
        }
        return getPlotSeriesPane(ChartTypeManager.CHART_PRIORITY, parent, plot);
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
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String priority = (String) entry.getKey();
            if (chartTypeInterfaces.get(priority).containsKey(plotID)){
                return isUseDefaultPane(priority, plotID);
            }
        }

        return true;
    }

    private boolean isUseDefaultPane(String priority, String plotID){

        if (chartTypeInterfaces.containsKey(priority) && chartTypeInterfaces.get(priority).containsKey(plotID)) {
            return chartTypeInterfaces.get(priority).get(plotID).isUseDefaultPane();
        }

        return true;
    }

    public void readXML(XMLableReader reader) {
        readXML(reader, null, PluginSimplify.NULL);
    }

    @Override
    public void readXML(XMLableReader reader, List<String> extraChartDesignInterfaceList, PluginSimplify simplify) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (extraChartDesignInterfaceList != null) {
                extraChartDesignInterfaceList.add(tagName);
            }
            if (IndependentChartUIProvider.XML_TAG.equals(tagName)) {
                addChartInterface(reader.getAttrAsString("class", ""), reader.getAttrAsString("priority", ChartTypeManager.DEFAULT_PRIORITY),reader.getAttrAsString("plotID", ""), simplify);
            }
        }
    }

    /**
     * 文件名
     *
     * @return 文件名
     */
    public String fileName() {
        return "chart.xml";
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {

    }
}