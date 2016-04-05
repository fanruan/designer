package com.fr.design;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chartinterface.*;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.file.XMLFileManager;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.plugin.PluginCollector;
import com.fr.plugin.PluginLicenseManager;
import com.fr.plugin.PluginMessage;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.Authorize;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.stable.plugin.PluginSimplify;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eason on 14/12/29.
 */
public class ChartTypeInterfaceManager extends XMLFileManager implements ExtraChartDesignClassManagerProvider {

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

    private static ChartTypeInterfaceManager classManager = null;

    private static LinkedHashMap<String, IndependentChartUIProvider> chartTypeInterfaces = new LinkedHashMap<String, IndependentChartUIProvider>();

    public synchronized static ChartTypeInterfaceManager getInstance() {
        if (classManager == null) {
            classManager = new ChartTypeInterfaceManager();
            chartTypeInterfaces.clear();
            classManager.readDefault();
            classManager.readXMLFile();
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

    private synchronized static void envChanged() {
        classManager = null;
    }

    private static void readDefault() {

        chartTypeInterfaces.put(ChartConstants.COLUMN_CHART, new ColumnIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.LINE_CHART, new LineIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.BAR_CHART, new BarIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.PIE_CHART, new PieIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.AREA_CHART, new AreaIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.SCATTER_CHART, new XYScatterIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.BUBBLE_CHART, new BubbleIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.RADAR_CHART, new RadarIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.STOCK_CHART, new StockIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.METER_CHART, new MeterIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.RANGE_CHART, new RangeIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.CUSTOM_CHART, new CustomIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.GANTT_CHART, new GanttIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.DONUT_CHART, new DonutIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.MAP_CHART, new MapIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.GIS_CHAER, new GisMapIndependentChartInterface());
        chartTypeInterfaces.put(ChartConstants.FUNNEL_CHART, new FunnelIndependentChartInterface());


    }

    public String getIconPath(String plotID) {
        return chartTypeInterfaces.get(plotID).getIconPath();
    }

    /**
     * 增加界面接口定义
     *
     * @param className 类名
     * @param plotID    标志ID
     */
    public void addChartInterface(String className, String plotID, PluginSimplify simplify) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class<?> clazz = loader.loadClass(className);
                Authorize authorize = clazz.getAnnotation(Authorize.class);
                if (authorize != null) {
                    PluginLicenseManager.getInstance().registerPaid(authorize, simplify);
                }
                IndependentChartUIProvider provider = (IndependentChartUIProvider) clazz.newInstance();
                if (PluginCollector.getCollector().isError(provider, IndependentChartUIProvider.CURRENT_API_LEVEL, simplify.getPluginName()) || !containsChart(plotID)) {
                    PluginMessage.remindUpdate(className);
                } else if (!chartTypeInterfaces.containsKey(plotID)) {
                    chartTypeInterfaces.put(plotID, provider);
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
            IndependentChartUIProvider creator = (IndependentChartUIProvider) entry.getValue();
            paneList.add(creator.getPlotTypePane());
        }

    }

    public AbstractChartAttrPane[] getAttrPaneArray(String plotID, AttributeChangeListener listener) {
        return chartTypeInterfaces.get(plotID).getAttrPaneArray(listener);
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return chartTypeInterfaces.get(plot.getPlotID()).getTableDataSourcePane(plot, parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return chartTypeInterfaces.get(plot.getPlotID()).getReportDataSourcePane(plot, parent);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return chartTypeInterfaces.get(plot.getPlotID()).getPlotConditionPane(plot);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return chartTypeInterfaces.get(plot.getPlotID()).getPlotSeriesPane(parent, plot);
    }

    /**
     * 是否使用默认的界面，为了避免界面来回切换
     *
     * @param plotID 序号
     * @return 是否使用默认的界面
     */
    public boolean isUseDefaultPane(String plotID) {

        if (chartTypeInterfaces.containsKey(plotID)) {
            return chartTypeInterfaces.get(plotID).isUseDefaultPane();
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
                addChartInterface(reader.getAttrAsString("class", ""), reader.getAttrAsString("plotID", ""), simplify);
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