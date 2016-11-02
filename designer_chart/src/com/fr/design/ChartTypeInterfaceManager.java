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
                if (chartTypeInterfaces.size() == 0) {
                    ChartTypeInterfaceManager.getInstance().readDefault();
                }
                //重新注册designModuleFactory
                DesignModuleFactory.registerExtraWidgetOptions(initWidgetOption());
            }
        });
    }

    public static WidgetOption[] initWidgetOption(){

        ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption[] child = new ChartWidgetOption[typeName.length];

        //异步加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                getWidgetOption(typeName, child);
            }
        }).start();

        return child;
    }

    private static void getWidgetOption(ChartInternationalNameContentBean[] typeName, ChartWidgetOption[] child){
        for (int i = 0; i < typeName.length; i++) {
            String plotID = typeName[i].getPlotID();
            Chart[] rowChart = ChartTypeManager.getInstance().getChartTypes(plotID);
            if(rowChart == null) {
                continue;
            }

            //加载初始化图表模型图片
            initChartsDemoImage(rowChart);

            String iconPath = ChartTypeInterfaceManager.getInstance().getIconPath(plotID);
            Icon icon = IOUtils.readIcon(iconPath);
            child[i] = new ChartWidgetOption(Inter.getLocText(typeName[i].getName()), icon, ChartEditor.class, rowChart[0]);
        }

        DesignModuleFactory.registerExtraWidgetOptions(child);
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

        chartTypeInterfaces.put(ChartTypeManager.DEFAULT_CHART_ID, chartUIList);
    }

    public String getIconPath(String plotID) {
        if (chartTypeInterfaces != null) {
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String chartID = (String) entry.getKey();
                String imagePath = getIconPath(chartID, plotID);
                if (!StringUtils.isEmpty(imagePath)) {
                    return imagePath;
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private String getIconPath(String chartID, String plotID) {
        if (chartTypeInterfaces.get(chartID) != null && chartTypeInterfaces.get(chartID).get(plotID) != null) {
            return chartTypeInterfaces.get(chartID).get(plotID).getIconPath();
        }else {
            return StringUtils.EMPTY;
        }
    }

    public static void addChartTypeInterface(IndependentChartUIProvider provider, String chartID, String plotID) {
        if (chartTypeInterfaces != null){
            if (!chartTypeInterfaces.containsKey(chartID)){
                //新建一个具体图表列表
                LinkedHashMap<String, IndependentChartUIProvider> chartUIList = new LinkedHashMap<String, IndependentChartUIProvider>();
                chartUIList.put(plotID, provider);
                chartTypeInterfaces.put(chartID, chartUIList);
            }else {
                LinkedHashMap<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(chartID);
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
    public void addChartInterface(String className, String chartID, String plotID, PluginSimplify simplify) {
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
                    ChartTypeInterfaceManager.getInstance().addChartTypeInterface(provider, chartID, plotID);
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
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String chartID = (String) entry.getKey();
            Iterator chartUIIterator = chartTypeInterfaces.get(chartID).entrySet().iterator();
            while (chartUIIterator.hasNext()) {
                Map.Entry chartUIEntry = (Map.Entry) chartUIIterator.next();
                IndependentChartUIProvider provider = (IndependentChartUIProvider) chartUIEntry.getValue();
                paneList.add(provider.getPlotTypePane());
            }
        }
    }

    public String[] getTitle4PopupWindow(String chartID){
        if (chartID.isEmpty()){
            return getTitle4PopupWindow();
        }
        String[] names = new String[getChartSize(chartID)];
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(chartID)){
            HashMap<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(chartID);
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
     * @param chartID
     * @return
     */
    public String getTitle4PopupWindow(String chartID, String plotID){
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(chartID) && chartTypeInterfaces.get(chartID).containsKey(plotID)){
            HashMap<String, IndependentChartUIProvider> chartUIList = chartTypeInterfaces.get(chartID);
            IndependentChartUIProvider provider = chartTypeInterfaces.get(chartID).get(plotID);
            return   provider.getPlotTypePane().title4PopupWindow();

        }
        return new String();
    }

    private String[] getTitle4PopupWindow(){
        int size = 0;
        if (chartTypeInterfaces != null){
            Iterator iterator = chartTypeInterfaces.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                String chartID = (String) entry.getKey();
                size += getChartSize(chartID);
            }
            String[] names = new String[size];

            int index = 0;

            //处理vanChart
            Iterator vanChartUI = chartTypeInterfaces.get(ChartTypeManager.vanChartID).entrySet().iterator();
            index = fetchNames(vanChartUI, names, index);
            //处理chart
            Iterator chartUI = chartTypeInterfaces.get(ChartTypeManager.chartID).entrySet().iterator();
            index = fetchNames(chartUI, names, index);
            //其它图表
            Iterator i = chartTypeInterfaces.entrySet().iterator();
            while (i.hasNext()){
                Map.Entry entry = (Map.Entry) i.next();
                String chartID = (String) entry.getKey();
                if (!(ComparatorUtils.equals(chartID, ChartTypeManager.chartID) || ComparatorUtils.equals(chartID, ChartTypeManager.vanChartID))) {
                    Iterator otherChartUI = chartTypeInterfaces.get(chartID).entrySet().iterator();
                    index = fetchNames(otherChartUI, names, index);
                }
            }
            return names;
        }

        return new String[0];
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
            String chartID = (String) entry.getKey();
            if (plotInChart(plotID, chartID)) {
                return getChartDataPane(chartID, plotID, listener);
            }
        }
        return getChartDataPane(ChartTypeManager.DEFAULT_CHART_ID, plotID, listener);
    }

    private ChartDataPane getChartDataPane(String chartID, String plotID, AttributeChangeListener listener) {
        return chartTypeInterfaces.get(chartID).get(plotID).getChartDataPane(listener);
    }

    /**
     * 获取对应ID的图表数量
     * @param chartID
     * @return
     */
    private int getChartSize(String chartID){
        if (chartTypeInterfaces != null && chartTypeInterfaces.containsKey(chartID)){
            return chartTypeInterfaces.get(chartID).size();
        }
        return 0;
    }

    public AbstractChartAttrPane[] getAttrPaneArray(String plotID, AttributeChangeListener listener) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String chartID = (String) entry.getKey();
            if (plotInChart(plotID, chartID)) {
                return getAttrPaneArray(chartID, plotID, listener);
            }
        }
        return getAttrPaneArray(ChartTypeManager.DEFAULT_CHART_ID, plotID, listener);
    }

    private AbstractChartAttrPane[] getAttrPaneArray(String chartID, String plotID, AttributeChangeListener listener) {
        return chartTypeInterfaces.get(chartID).get(plotID).getAttrPaneArray(listener);
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String chartID = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), chartID)) {
                return getTableDataSourcePane(chartID, plot, parent);
            }
        }
        return getTableDataSourcePane(ChartTypeManager.DEFAULT_CHART_ID, plot, parent);
    }

    private AbstractTableDataContentPane getTableDataSourcePane(String chartID, Plot plot, ChartDataPane parent) {
        return chartTypeInterfaces.get(chartID).get(plot.getPlotID()).getTableDataSourcePane(plot, parent);
    }


    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String chartID = (String) entry.getKey();
            String plotID = plot.getPlotID();
            if (plotInChart(plotID, chartID)) {
                return getReportDataSourcePane(chartID, plot, parent);
            }
        }
        return getReportDataSourcePane(ChartTypeManager.DEFAULT_CHART_ID, plot, parent);
    }

    private boolean plotInChart(String plotID, String chartID) {
        return chartTypeInterfaces != null
                && chartTypeInterfaces.containsKey(chartID)
                && chartTypeInterfaces.get(chartID).containsKey(plotID);
    }

    private AbstractReportDataContentPane getReportDataSourcePane(String chartID, Plot plot, ChartDataPane parent) {
        return chartTypeInterfaces.get(chartID).get(plot.getPlotID()).getReportDataSourcePane(plot, parent);
    }


    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String chartID = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), chartID)) {
                return getPlotConditionPane(chartID, plot);
            }
        }
        return getPlotConditionPane(ChartTypeManager.DEFAULT_CHART_ID, plot);
    }

    private ConditionAttributesPane getPlotConditionPane(String chartID, Plot plot) {
        return chartTypeInterfaces.get(chartID).get(plot.getPlotID()).getPlotConditionPane(plot);
    }


    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        Iterator iterator = chartTypeInterfaces.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String chartID = (String) entry.getKey();
            if (plotInChart(plot.getPlotID(), chartID)) {
                return getPlotSeriesPane(chartID, parent, plot);
            }
        }
        return getPlotSeriesPane(ChartTypeManager.DEFAULT_CHART_ID, parent, plot);
    }

    private BasicBeanPane<Plot> getPlotSeriesPane(String chartID, ChartStylePane parent, Plot plot) {
        return chartTypeInterfaces.get(chartID).get(plot.getPlotID()).getPlotSeriesPane(parent, plot);
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
            String chartID = (String) entry.getKey();
            if (chartTypeInterfaces.get(chartID).containsKey(plotID)){
                return isUseDefaultPane(chartID, plotID);
            }
        }

        return true;
    }

    private boolean isUseDefaultPane(String chartID, String plotID){

        if (chartTypeInterfaces.containsKey(chartID) && chartTypeInterfaces.get(chartID).containsKey(plotID)) {
            return chartTypeInterfaces.get(chartID).get(plotID).isUseDefaultPane();
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
                addChartInterface(reader.getAttrAsString("class", ""), reader.getAttrAsString("chartID", ChartTypeManager.DEFAULT_CHART_ID),reader.getAttrAsString("plotID", ""), simplify);
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