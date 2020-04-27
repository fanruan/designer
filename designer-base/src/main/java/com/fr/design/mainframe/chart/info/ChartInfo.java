package com.fr.design.mainframe.chart.info;

import com.fr.base.io.BaseBook;
import com.fr.chartx.attr.ChartProvider;
import com.fr.chartx.config.info.AbstractConfig;
import com.fr.chartx.config.info.ChartConfigInfo;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.config.MarketConfig;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.mainframe.burying.point.AbstractPointInfo;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralUtils;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-17
 */
public class ChartInfo extends AbstractPointInfo {
    public static final String XML_TAG = "ChartInfo";
    private static final String CHART_CONSUMING_URL = CloudCenter.getInstance().acquireUrlByKind("chartinfo.consuming") + "/single";
    private static final String CHART_FUNCTION_URL = CloudCenter.getInstance().acquireUrlByKind("chart.info.function") + "/single";

    private static final String XML_CHART_CONSUMING_MAP = "chartConsumingMap";
    private static final String ATTR_TEST_TEMPLATE = "testTemplate";
    private static final String ATTR_TEST_CHART = "testChart";
    private static final String ATTR_DAY_COUNT = "day_count";
    private static final String ATTR_USERNAME = "username";
    private static final String ATTR_UUID = "uuid";
    private static final String ATTR_ACTIVITYKEY = "activityKey";
    private static final String ATTR_TEMPLATE_ID = "templateID";
    private static final String ATTR_REPORT_TYPE = "type";
    private static final String ATTR_CHART_ID = "chartId";
    private static final String ATTR_CHART_TYPE = "chartType";
    private static final String ATTR_CHART_CREATE_TIME = "chartCreateTime";
    private static final String ATTR_CHART_TYPE_TIME = "chartTypeTime";
    private static final String ATTR_CHART_PROPERTY_FIRST_TIME = "chartPropertyFirstTime";
    private static final String ATTR_CHART_PROPERTY_END_TIME = "chartPropertyEndTime";
    private static final String ATTR_JAR_TIME = "jarTime";
    private static final String ATTR_VERSION = "version";
    private static final String ATTR_USER_ID = "userId";
    private static final String ATTR_FIRST_CHART_TYPE = "firstChartType";
    private static final String ATTR_OVER_CHART_TYPE_COUNT = "overChartTypeCount";
    private static final String ATTR_IS_NEW = "isNew";
    private static final String ATTR_IS_REUSE = "isReuse";

    private static final int COMPLETE_DAY_COUNT = 3;  // 判断图表是否可以上传的天数

    private String chartId = StringUtils.EMPTY;

    private String templateId = StringUtils.EMPTY;

    private Map<String, String> chartConsumingMap = new HashMap<>();

    private ChartConfigInfo chartConfigInfo = new ChartConfigInfo();

    private BaseBook book;

    private boolean testTemplate;

    private boolean testChart;

    private ChartInfo() {
    }

    private ChartInfo(String chartId, String templateId, BaseBook book) {
        this.chartId = chartId;
        this.templateId = templateId;
        this.book = book;
        this.testChart = true;
    }

    public String getChartId() {
        return chartId;
    }

    @Override
    protected String key() {
        return chartId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
        this.chartConsumingMap.put(ATTR_TEMPLATE_ID, templateId);
    }

    public BaseBook getBook() {
        return book;
    }

    @Override
    public boolean isTestTemplate() {
        return testTemplate || testChart;
    }

    public void setTestTemplate(boolean testTemplate) {
        this.testTemplate = testTemplate;
    }

    public void setTestChart(boolean testChart) {
        this.testChart = testChart;
    }

    static ChartInfo newInstanceByRead(XMLableReader reader) {
        ChartInfo chartInfo = new ChartInfo();
        reader.readXMLObject(chartInfo);
        return chartInfo;
    }

    public static ChartInfo newInstance(ChartProvider chartProvider) {
        return newInstance(chartProvider, null, false, false);
    }

    public static ChartInfo newInstance(ChartProvider chartProvider, String createTime, boolean isNew, boolean isReuse) {
        HashMap<String, String> chartConsumingMap = new HashMap<>();

        String username = MarketConfig.getInstance().getBbsUsername();
        String userId = String.valueOf(MarketConfig.getInstance().getBbsUid());
        String uuid = DesignerEnvManager.getEnvManager().getUUID();
        String activityKey = DesignerEnvManager.getEnvManager().getActivationKey();
        String chartId = chartProvider.getChartUuid();
        String chartType = chartProvider.getID();

        BaseBook book = DesignModelAdapter.getCurrentModelAdapter().getBook();
        String templateId = book.getTemplateID();
        int reportType = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().getProcessInfo().getReportType();

        String typeTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

        createTime = createTime == null ? typeTime : createTime;

        String jarTime = GeneralUtils.readBuildNO();
        String version = ProductConstants.VERSION;
        chartConsumingMap.put(ATTR_USERNAME, username);
        chartConsumingMap.put(ATTR_UUID, uuid);
        chartConsumingMap.put(ATTR_ACTIVITYKEY, activityKey);
        chartConsumingMap.put(ATTR_TEMPLATE_ID, templateId);
        chartConsumingMap.put(ATTR_REPORT_TYPE, String.valueOf(reportType));
        chartConsumingMap.put(ATTR_CHART_ID, chartId);
        chartConsumingMap.put(ATTR_CHART_TYPE, chartType);
        chartConsumingMap.put(ATTR_CHART_CREATE_TIME, createTime);
        chartConsumingMap.put(ATTR_CHART_TYPE_TIME, typeTime);
        chartConsumingMap.put(ATTR_CHART_PROPERTY_FIRST_TIME, "");
        chartConsumingMap.put(ATTR_CHART_PROPERTY_END_TIME, "");
        chartConsumingMap.put(ATTR_JAR_TIME, jarTime);
        chartConsumingMap.put(ATTR_VERSION, version);
        chartConsumingMap.put(ATTR_USER_ID, userId);
        chartConsumingMap.put(ATTR_FIRST_CHART_TYPE, chartType);
        chartConsumingMap.put(ATTR_OVER_CHART_TYPE_COUNT, "0");
        chartConsumingMap.put(ATTR_IS_NEW, String.valueOf(isNew));
        chartConsumingMap.put(ATTR_IS_REUSE, String.valueOf(isReuse));

        ChartInfo chartInfo = new ChartInfo(chartId, templateId, book);
        chartInfo.chartConsumingMap = chartConsumingMap;
        chartProvider.initChartConfigInfo(chartInfo.chartConfigInfo);

        return chartInfo;
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        if (StringUtils.isNotEmpty(chartId)) {
            writer.attr(ATTR_CHART_ID, this.chartId);
        }
        if (StringUtils.isNotEmpty(templateId)) {
            writer.attr(ATTR_TEMPLATE_ID, this.templateId);
        }
        if (idleDayCount >= 0) {
            writer.attr(ATTR_DAY_COUNT, this.idleDayCount);
        }
        writer.attr(ATTR_TEST_TEMPLATE, this.testTemplate);
        writer.attr(ATTR_TEST_CHART, this.testChart);
        writer.startTAG(XML_CHART_CONSUMING_MAP);
        writer.attr(ATTR_USERNAME, chartConsumingMap.get(ATTR_USERNAME));
        writer.attr(ATTR_UUID, chartConsumingMap.get(ATTR_UUID));
        writer.attr(ATTR_ACTIVITYKEY, chartConsumingMap.get(ATTR_ACTIVITYKEY));
        writer.attr(ATTR_REPORT_TYPE, chartConsumingMap.get(ATTR_REPORT_TYPE));
        writer.attr(ATTR_CHART_TYPE, chartConsumingMap.get(ATTR_CHART_TYPE));
        writer.attr(ATTR_CHART_CREATE_TIME, chartConsumingMap.get(ATTR_CHART_CREATE_TIME));
        writer.attr(ATTR_CHART_TYPE_TIME, chartConsumingMap.get(ATTR_CHART_TYPE_TIME));
        writer.attr(ATTR_CHART_PROPERTY_FIRST_TIME, chartConsumingMap.get(ATTR_CHART_PROPERTY_FIRST_TIME));
        writer.attr(ATTR_CHART_PROPERTY_END_TIME, chartConsumingMap.get(ATTR_CHART_PROPERTY_END_TIME));
        writer.attr(ATTR_JAR_TIME, chartConsumingMap.get(ATTR_JAR_TIME));
        writer.attr(ATTR_VERSION, chartConsumingMap.get(ATTR_VERSION));
        writer.attr(ATTR_USER_ID, chartConsumingMap.get(ATTR_USER_ID));
        writer.attr(ATTR_FIRST_CHART_TYPE, chartConsumingMap.get(ATTR_FIRST_CHART_TYPE));
        writer.attr(ATTR_OVER_CHART_TYPE_COUNT, chartConsumingMap.get(ATTR_OVER_CHART_TYPE_COUNT));
        writer.attr(ATTR_IS_NEW, chartConsumingMap.get(ATTR_IS_NEW));
        writer.attr(ATTR_IS_REUSE, chartConsumingMap.get(ATTR_IS_REUSE));
        writer.end();

        chartConfigInfo.writeXML(writer);
        writer.end();
    }

    @Override
    public void readXML(XMLableReader reader) {

        if (!reader.isChildNode()) {
            idleDayCount = reader.getAttrAsInt(ATTR_DAY_COUNT, 0);
            chartId = reader.getAttrAsString(ATTR_CHART_ID, StringUtils.EMPTY);
            templateId = reader.getAttrAsString(ATTR_TEMPLATE_ID, StringUtils.EMPTY);
            testTemplate = reader.getAttrAsBoolean(ATTR_TEST_TEMPLATE, true);
            testChart = reader.getAttrAsBoolean(ATTR_TEST_CHART, false);
        } else {
            String name = reader.getTagName();
            if (XML_CHART_CONSUMING_MAP.equals(name)) {
                chartConsumingMap.put(ATTR_USERNAME, reader.getAttrAsString(ATTR_USERNAME, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_UUID, reader.getAttrAsString(ATTR_UUID, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_ACTIVITYKEY, reader.getAttrAsString(ATTR_ACTIVITYKEY, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_TEMPLATE_ID, templateId);
                chartConsumingMap.put(ATTR_REPORT_TYPE, reader.getAttrAsString(ATTR_REPORT_TYPE, "0"));
                chartConsumingMap.put(ATTR_CHART_ID, chartId);
                chartConsumingMap.put(ATTR_CHART_TYPE, reader.getAttrAsString(ATTR_CHART_TYPE, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_CHART_CREATE_TIME, reader.getAttrAsString(ATTR_CHART_CREATE_TIME, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_CHART_TYPE_TIME, reader.getAttrAsString(ATTR_CHART_TYPE_TIME, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_CHART_PROPERTY_FIRST_TIME, reader.getAttrAsString(ATTR_CHART_PROPERTY_FIRST_TIME, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_CHART_PROPERTY_END_TIME, reader.getAttrAsString(ATTR_CHART_PROPERTY_END_TIME, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_JAR_TIME, reader.getAttrAsString(ATTR_JAR_TIME, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_VERSION, reader.getAttrAsString(ATTR_VERSION, "8.0"));
                chartConsumingMap.put(ATTR_USER_ID, reader.getAttrAsString(ATTR_USER_ID, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_FIRST_CHART_TYPE, reader.getAttrAsString(ATTR_FIRST_CHART_TYPE, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_OVER_CHART_TYPE_COUNT, reader.getAttrAsString(ATTR_OVER_CHART_TYPE_COUNT, "0"));
                chartConsumingMap.put(ATTR_IS_NEW, reader.getAttrAsString(ATTR_IS_NEW, StringUtils.EMPTY));
                chartConsumingMap.put(ATTR_IS_REUSE, reader.getAttrAsString(ATTR_IS_REUSE, StringUtils.EMPTY));
            }
            if (ChartConfigInfo.XML_TAG.equals(name)) {
                this.chartConfigInfo = (ChartConfigInfo) reader.readXMLObject(new ChartConfigInfo());
            }
        }
    }

    @Override
    public boolean isComplete() {
        // 连续3天打开了设计器但是没有编辑
        return idleDayCount > COMPLETE_DAY_COUNT;
    }

    @Override
    public Map<String, String> getSendInfo() {
        Map<String, String> sendMap = new HashMap<>();
        sendMap.put(CHART_CONSUMING_URL, new JSONObject(chartConsumingMap).toString());
        sendMap.put(CHART_FUNCTION_URL, getFunctionJson());
        return sendMap;
    }

    private String getFunctionJson() {
        JSONObject jsonObject = JSONObject.create();
        jsonObject.put("chartID", this.chartId);
        chartConfigInfo.toJSONObject(jsonObject);
        return jsonObject.toString();
    }

    public void updatePropertyTime() {
        String propertyTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isEmpty(chartConsumingMap.get(ATTR_CHART_PROPERTY_FIRST_TIME))) {
            chartConsumingMap.put(ATTR_CHART_PROPERTY_FIRST_TIME, propertyTime);
        }
        chartConsumingMap.put(ATTR_CHART_PROPERTY_END_TIME, propertyTime);
    }

    public void updateChartType(ChartProvider chartProvider) {
        String typeTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

        chartConsumingMap.put(ATTR_CHART_TYPE_TIME, typeTime);
        chartConsumingMap.put(ATTR_CHART_TYPE, chartProvider.getID());
        chartConsumingMap.put(ATTR_CHART_PROPERTY_FIRST_TIME, "");
        chartConsumingMap.put(ATTR_CHART_PROPERTY_END_TIME, "");
        String count = chartConsumingMap.get(ATTR_OVER_CHART_TYPE_COUNT);
        count = StringUtils.isEmpty(count) ? "1" : String.valueOf(Integer.parseInt(count) + 1);
        chartConsumingMap.put(ATTR_OVER_CHART_TYPE_COUNT, count);

        resetChartConfigInfo(chartProvider);
    }

    public void resetChartConfigInfo(ChartProvider chartProvider) {
        chartConfigInfo.reset();
        chartProvider.initChartConfigInfo(chartConfigInfo);
    }

    public void updateFirstType(String chartType) {
        chartConsumingMap.put(ATTR_FIRST_CHART_TYPE, chartType);
    }

    public void updateChartConfig(ConfigType configType, AbstractConfig config) {
        chartConfigInfo.updateChartConfig(configType, config);
    }

    @Override
    public ChartInfo clone() {
        ChartInfo chartInfo = new ChartInfo();
        chartInfo.chartId = this.chartId;
        chartInfo.idleDayCount = this.idleDayCount;
        chartInfo.templateId = this.templateId;
        chartInfo.testTemplate = this.testTemplate;
        Map<String, String> chartConsumingMap = new HashMap<>();
        for (Map.Entry<String, String> entry : this.chartConsumingMap.entrySet()) {
            chartConsumingMap.put(entry.getKey(), entry.getValue());
        }
        chartInfo.chartConsumingMap = chartConsumingMap;
        chartInfo.chartConfigInfo = chartConfigInfo.clone();
        return chartInfo;
    }
}
