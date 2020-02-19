package com.fr.design.mainframe.chart.info;

import com.fr.base.FRContext;
import com.fr.base.io.BaseBook;
import com.fr.base.io.XMLReadHelper;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.template.info.SendHelper;
import com.fr.design.mainframe.template.info.TemplateInfo;
import com.fr.design.mainframe.template.info.TemplateProcessInfo;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import com.fr.third.org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-18
 */
public class ChartInfoCollector implements XMLReadable, XMLWriter {
    private static final String XML_TAG = "ChartInfoCollector";
    private static final String XML_LAST_EDIT_DAY = "lastEditDay";

    private static final String XML_CHART_INFO_LIST = "ChartInfoList";
    private static final String XML_FILE_NAME = "chart.info";

    private static final int VALID_CELL_COUNT = 5;  // 有效报表模板的格子数
    private static final int VALID_WIDGET_COUNT = 5;  // 有效报表模板的控件数

    private static ChartInfoCollector instance;
    private static final int MAX_SIZE = 512 * 1024 * 1024;
    private Map<String, ChartInfo> chartInfoMap;

    private Map<String, ChartInfo> chartInfoCacheMap;

    private String lastEditDay = StringUtils.EMPTY;

    private ChartInfoCollector() {
        init();
    }

    private void init() {
        chartInfoMap = new ConcurrentHashMap<>();
        chartInfoCacheMap = new HashMap<>();
        loadFromFile();
    }

    public static ChartInfoCollector getInstance() {
        if (instance == null) {
            instance = new ChartInfoCollector();
        }
        return instance;
    }

    /**
     * 新建图表，保存状态
     */
    public void collection(String chartId, String chartType, String createTime) {
        if (!shouldCollectInfo()) {
            return;
        }

        ChartInfo chartInfo = ChartInfo.newInstance(chartId, chartType, createTime);
        chartInfoCacheMap.put(chartId, chartInfo);
    }

    /**
     * 图表编辑，更新编辑时间
     */
    public void updateChartPropertyTime(String chartId, String chartType) {
        if (!shouldCollectInfo()) {
            return;
        }
        ChartInfo chartInfo = getOrCreateChartInfo(chartId, chartType);

        //更新编辑时间
        chartInfo.updatePropertyTime();

        //重置计数
        chartInfo.resetIdleDayCount();
    }

    /**
     * 图表类型变化，更新类型和类型确认时间
     */
    public void updateChartTypeTime(String chartId, String chartType) {
        if (!shouldCollectInfo()) {
            return;
        }

        ChartInfo chartInfo = getOrCreateChartInfo(chartId, chartType);

        //更新类型确认时间和类型
        chartInfo.updateChartType(chartType);

        //重置计数
        chartInfo.resetIdleDayCount();
    }

    private ChartInfo getOrCreateChartInfo(String chartId, String chartType) {
        //缓存中有从缓存中拿
        if (chartInfoCacheMap.containsKey(chartId)) {
            return chartInfoCacheMap.get(chartId);
        }
        //缓存中没有从文件中读取的信息中拷贝到缓存
        if (chartInfoMap.containsKey(chartId)) {
            ChartInfo chartInfo = chartInfoMap.get(chartId).clone();
            chartInfoCacheMap.put(chartId, chartInfo);
            return chartInfo;
        }
        //都有的话创建一个并加入到缓存中
        ChartInfo chartInfo = ChartInfo.newInstance(chartId, chartType);
        chartInfoCacheMap.put(chartId, chartInfo);
        return chartInfo;
    }

    /**
     * 保存模板的时候将该模板中的图表埋点信息保存
     */
    public void collectInfo(String templateId, String originID, TemplateProcessInfo processInfo) {
        if (!shouldCollectInfo()) {
            return;
        }
        if (StringUtils.isEmpty(originID)) {
            originID = templateId;
        }
        boolean testTemplate = isTestTemplate(processInfo);

        for (ChartInfo chartInfo : chartInfoMap.values()) {
            if (originID.equals(chartInfo.getTemplateId())) {
                chartInfo.setTemplateId(templateId);
                chartInfo.setTestTemplate(testTemplate);
            }
        }

        for (ChartInfo chartInfo : chartInfoCacheMap.values()) {
            BaseBook book = chartInfo.getBook();
            if ((book != null && templateId.equals(book.getTemplateID())) ||
                    originID.equals(chartInfo.getTemplateId())) {
                chartInfo.setTemplateId(templateId);
                chartInfo.setTestTemplate(testTemplate);
                chartInfoMap.put(chartInfo.getChartId(), chartInfo);
            }
        }



        // 每次更新之后，都同步到暂存文件中
        saveInfo();
    }

    private boolean isTestTemplate(TemplateProcessInfo processInfo) {
        int reportType = processInfo.getReportType();
        int cellCount = processInfo.getCellCount();
        int floatCount = processInfo.getFloatCount();
        int blockCount = processInfo.getBlockCount();
        int widgetCount = processInfo.getWidgetCount();

        return TemplateInfo.judgeTestTemplate(reportType, cellCount, floatCount, blockCount, widgetCount);
    }

    /**
     * 发送本地图表信息到服务器，并清空已发送图表的本地记录
     */
    public void sendChartInfo() {

        addIdleDayCount();

        List<String> removeLaterList = new ArrayList<>();

        for (String key : chartInfoMap.keySet()) {
            ChartInfo chartInfo = chartInfoMap.get(key);
            if (chartInfo.isComplete()) {
                if (!chartInfo.isTestTemplate()) {
                    if (SendHelper.sendChartInfo(chartInfo)) {
                        removeLaterList.add(key);
                    }
                } else {
                    removeLaterList.add(key);
                }
            }
        }

        // 清空记录
        for (String key : removeLaterList) {
            chartInfoMap.remove(key);
        }

        saveInfo();
    }

    /**
     * 更新 day_count：打开设计器却未编辑图表的连续日子
     */
    private void addIdleDayCount() {
        // 判断今天是否第一次打开设计器，为了防止同一天内，多次 addIdleDayCount
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        if (ComparatorUtils.equals(today, lastEditDay)) {
            return;
        }
        for (ChartInfo chartInfo : chartInfoMap.values()) {
            chartInfo.addIdleDayCountByOne();
        }
        lastEditDay = today;
    }

    private void loadFromFile() {
        if (!getInfoFile().exists()) {
            return;
        }

        XMLableReader reader = null;
        try (InputStream in = new FileInputStream(getInfoFile())) {
            // XMLableReader 还是应该考虑实现 Closable 接口的，这样就能使用 try-with 语句了
            reader = XMLReadHelper.createXMLableReader(in, XMLPrintWriter.XML_ENCODER);
            if (reader == null) {
                return;
            }
            reader.readXMLObject(this);
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (XMLStreamException | IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        File file = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), XML_FILE_NAME));
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
        }
        return file;
    }

    private boolean shouldCollectInfo() {
        return FileUtils.sizeOf(getInfoFile()) <= MAX_SIZE && DesignerEnvManager.getEnvManager().isJoinProductImprove() && FRContext.isChineseEnv();
    }


    /**
     * 将包含所有信息的对象保存到文件
     */
    private void saveInfo() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLTools.writeOutputStreamXML(this, out);
            out.flush();
            out.close();
            String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(getInfoFile(), fileContent, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage());
        }
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            try {
                String name = reader.getTagName();
                if (ChartInfo.XML_TAG.equals(name)) {
                    ChartInfo chartInfo = ChartInfo.newInstanceByRead(reader);
                    chartInfoMap.put(chartInfo.getChartId(), chartInfo);
                } else if (XML_LAST_EDIT_DAY.equals(name)) {
                    lastEditDay = reader.getElementValue();
                }
            } catch (Exception ex) {
                // 什么也不做，使用默认值
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG(XML_LAST_EDIT_DAY);
        writer.textNode(lastEditDay);
        writer.end();

        writer.startTAG(XML_CHART_INFO_LIST);
        for (ChartInfo chartInfo : chartInfoMap.values()) {
            chartInfo.writeXML(writer);
        }
        writer.end();

        writer.end();
    }
}
