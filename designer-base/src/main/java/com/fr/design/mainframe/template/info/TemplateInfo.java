package com.fr.design.mainframe.template.info;

import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.burying.point.AbstractPointInfo;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralUtils;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 对应一张模版的记录
 * Created by plough on 2019/4/18.
 */
public class TemplateInfo extends AbstractPointInfo {
    static final String XML_TAG = "TemplateInfo";

    private static final String CONSUMING_URL = CloudCenter.getInstance().acquireUrlByKind("tempinfo.consuming") + "/single";
    private static final String PROCESS_URL = CloudCenter.getInstance().acquireUrlByKind("tempinfo.process") + "/single";

    private static final String XML_PROCESS_MAP = "processMap";
    private static final String XML_CONSUMING_MAP = "consumingMap";
    private static final String ATTR_DAY_COUNT = "day_count";
    private static final String ATTR_TEMPLATE_ID = "templateID";
    private static final String ATTR_ORIGIN_ID = "originID";
    private static final String ATTR_PROCESS = "process";
    private static final String ATTR_FLOAT_COUNT = "float_count";
    private static final String ATTR_WIDGET_COUNT = "widget_count";
    private static final String ATTR_CELL_COUNT = "cell_count";
    private static final String ATTR_BLOCK_COUNT = "block_count";
    private static final String ATTR_REPORT_TYPE = "report_type";
    private static final String ATTR_ACTIVITYKEY = "activitykey";
    private static final String ATTR_JAR_TIME = "jar_time";
    private static final String ATTR_CREATE_TIME = "create_time";
    private static final String ATTR_UUID = "uuid";
    private static final String ATTR_TIME_CONSUME = "time_consume";
    private static final String ATTR_ORIGIN_TIME = "originTime";
    private static final String ATTR_VERSION = "version";
    private static final String ATTR_USERNAME = "username";

    private static final int VALID_CELL_COUNT = 5;  // 有效报表模板的格子数
    private static final int VALID_WIDGET_COUNT = 5;  // 有效报表模板的控件数
    private static final int COMPLETE_DAY_COUNT = 15;  // 判断模板是否完成的天数

    private String templateID = StringUtils.EMPTY;
    private String originID = StringUtils.EMPTY;
    // todo: processMap 和 consumingMap 还可以再拆解为小类，以后继续重构
    private Map<String, Object> processMap = new HashMap<>();
    private Map<String, Object> consumingMap = new HashMap<>();

    private TemplateInfo() {
    }

    private TemplateInfo(String templateID, String originID) {
        this.templateID = templateID;
        this.originID = originID;
    }

    @Override
    protected String key() {
        return templateID;
    }

    public static TemplateInfo newInstanceByRead(XMLableReader reader) {
        TemplateInfo templateInfo = new TemplateInfo();
        reader.readXMLObject(templateInfo);
        return templateInfo;
    }

    public static TemplateInfo newInstance(String templateID) {
        return newInstance(templateID, StringUtils.EMPTY, 0);
    }

    public static TemplateInfo newInstance(String templateID, String originID, int originTime) {
        HashMap<String, Object> consumingMap = new HashMap<>();

        String username = MarketConfig.getInstance().getBbsUsername();
        String uuid = DesignerEnvManager.getEnvManager().getUUID();
        String activitykey = DesignerEnvManager.getEnvManager().getActivationKey();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        String jarTime = GeneralUtils.readBuildNO();
        String version = ProductConstants.VERSION;
        consumingMap.put(ATTR_USERNAME, username);
        consumingMap.put(ATTR_UUID, uuid);
        consumingMap.put(ATTR_ACTIVITYKEY, activitykey);
        consumingMap.put(ATTR_TEMPLATE_ID, templateID);
        consumingMap.put(ATTR_ORIGIN_ID, originID);
        consumingMap.put(ATTR_CREATE_TIME, createTime);
        consumingMap.put(ATTR_TIME_CONSUME, originTime);  // timeConsume 在原来模版的基础上累加
        consumingMap.put(ATTR_ORIGIN_TIME, originTime);
        consumingMap.put(ATTR_JAR_TIME, jarTime);
        consumingMap.put(ATTR_VERSION, version);

        TemplateInfo templateInfo = new TemplateInfo(templateID, originID);
        templateInfo.consumingMap = consumingMap;

        return templateInfo;
    }

    String getTemplateID() {
        return templateID;
    }

    String getOriginID() {
        return originID;
    }

    int getTimeConsume() {
        return (int) consumingMap.get(ATTR_TIME_CONSUME);
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        if (StringUtils.isNotEmpty(templateID)) {
            writer.attr(ATTR_TEMPLATE_ID, this.templateID);
        }
        if (StringUtils.isNotEmpty(originID)) {
            writer.attr(ATTR_ORIGIN_ID, this.originID);
        }
        if (idleDayCount >= 0) {
            writer.attr(ATTR_DAY_COUNT, this.idleDayCount);
        }
        writeProcessMap(writer);
        writeConsumingMap(writer);

        writer.end();
    }

    private void writeProcessMap(XMLPrintWriter writer) {
        writer.startTAG(XML_PROCESS_MAP);
        writer.attr(ATTR_PROCESS, (String) processMap.get(ATTR_PROCESS));
        writer.attr(ATTR_FLOAT_COUNT, (int) processMap.get(ATTR_FLOAT_COUNT));
        writer.attr(ATTR_WIDGET_COUNT, (int) processMap.get(ATTR_WIDGET_COUNT));
        writer.attr(ATTR_CELL_COUNT, (int) processMap.get(ATTR_CELL_COUNT));
        writer.attr(ATTR_BLOCK_COUNT, (int) processMap.get(ATTR_BLOCK_COUNT));
        writer.attr(ATTR_REPORT_TYPE, (int) processMap.get(ATTR_REPORT_TYPE));
        writer.end();
    }

    private void writeConsumingMap(XMLPrintWriter writer) {
        writer.startTAG(XML_CONSUMING_MAP);
        writer.attr(ATTR_ACTIVITYKEY, (String) consumingMap.get(ATTR_ACTIVITYKEY));
        writer.attr(ATTR_JAR_TIME, (String) consumingMap.get(ATTR_JAR_TIME));
        writer.attr(ATTR_CREATE_TIME, (String) consumingMap.get(ATTR_CREATE_TIME));
        writer.attr(ATTR_UUID, (String) consumingMap.get(ATTR_UUID));
        writer.attr(ATTR_TIME_CONSUME, (int) consumingMap.get(ATTR_TIME_CONSUME));
        writer.attr(ATTR_ORIGIN_TIME, (int) consumingMap.get(ATTR_ORIGIN_TIME));
        writer.attr(ATTR_VERSION, (String) consumingMap.get(ATTR_VERSION));
        writer.attr(ATTR_USERNAME, (String) consumingMap.get(ATTR_USERNAME));
        writer.end();
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (!reader.isChildNode()) {
            idleDayCount = reader.getAttrAsInt(ATTR_DAY_COUNT, 0);
            templateID = reader.getAttrAsString(ATTR_TEMPLATE_ID, StringUtils.EMPTY);
            originID = reader.getAttrAsString(ATTR_ORIGIN_ID, StringUtils.EMPTY);
        } else {
            try {
                String name = reader.getTagName();
                if (XML_PROCESS_MAP.equals(name)) {
                    processMap.put(ATTR_PROCESS, reader.getAttrAsString(ATTR_PROCESS, StringUtils.EMPTY));
                    processMap.put(ATTR_FLOAT_COUNT, reader.getAttrAsInt(ATTR_FLOAT_COUNT, 0));
                    processMap.put(ATTR_WIDGET_COUNT, reader.getAttrAsInt(ATTR_WIDGET_COUNT, 0));
                    processMap.put(ATTR_CELL_COUNT, reader.getAttrAsInt(ATTR_CELL_COUNT, 0));
                    processMap.put(ATTR_BLOCK_COUNT, reader.getAttrAsInt(ATTR_BLOCK_COUNT, 0));
                    processMap.put(ATTR_REPORT_TYPE, reader.getAttrAsInt(ATTR_REPORT_TYPE, 0));
                    processMap.put(ATTR_TEMPLATE_ID, templateID);
                } else if (XML_CONSUMING_MAP.equals(name)) {
                    consumingMap.put(ATTR_ACTIVITYKEY, reader.getAttrAsString(ATTR_ACTIVITYKEY, StringUtils.EMPTY));
                    consumingMap.put(ATTR_JAR_TIME, reader.getAttrAsString(ATTR_JAR_TIME, StringUtils.EMPTY));
                    consumingMap.put(ATTR_CREATE_TIME, reader.getAttrAsString(ATTR_CREATE_TIME, StringUtils.EMPTY));
                    consumingMap.put(ATTR_TEMPLATE_ID, templateID);
                    consumingMap.put(ATTR_ORIGIN_ID, originID);
                    consumingMap.put(ATTR_UUID, reader.getAttrAsString(ATTR_UUID, StringUtils.EMPTY));
                    consumingMap.put(ATTR_TIME_CONSUME, reader.getAttrAsInt(ATTR_TIME_CONSUME, 0));
                    consumingMap.put(ATTR_ORIGIN_TIME, reader.getAttrAsInt(ATTR_ORIGIN_TIME, 0));
                    consumingMap.put(ATTR_VERSION, reader.getAttrAsString(ATTR_VERSION, "8.0"));
                    consumingMap.put(ATTR_USERNAME, reader.getAttrAsString(ATTR_USERNAME, StringUtils.EMPTY));
                }
            } catch (Exception ex) {
                // 什么也不做，使用默认值
            }
        }
    }

    @Override
    protected boolean isTestTemplate() {
        if (!isComplete()) {
            return false;
        }

        int reportType = (int) processMap.get(ATTR_REPORT_TYPE);
        int cellCount = (int) processMap.get(ATTR_CELL_COUNT);
        int floatCount = (int) processMap.get(ATTR_FLOAT_COUNT);
        int blockCount = (int) processMap.get(ATTR_BLOCK_COUNT);
        int widgetCount = (int) processMap.get(ATTR_WIDGET_COUNT);
        return isTestTemplate(reportType, cellCount, floatCount, blockCount, widgetCount);
    }

    public static boolean isTestTemplate(int reportType, int cellCount, int floatCount, int blockCount, int widgetCount) {
        boolean isTestTemplate;
        if (reportType == 0) {  // 普通报表
            isTestTemplate = cellCount <= VALID_CELL_COUNT && floatCount <= 1 && widgetCount <= VALID_WIDGET_COUNT;
        } else if (reportType == 1) {  // 聚合报表
            isTestTemplate = blockCount <= 1 && widgetCount <= VALID_WIDGET_COUNT;
        } else {  // 表单(reportType == 2)
            isTestTemplate = widgetCount <= 1;
        }
        return isTestTemplate;
    }

    @Override
    protected boolean isComplete() {
        // 条件 1. 超过15天未编辑
        // 条件 2. 设计器在这段未编辑的时间内启动超过 X 次（目前定的 X = 3）。即"设计器最近 X 次启动的时间跨度" < "未编辑时间"；

        return idleDayCount > COMPLETE_DAY_COUNT
                && DesignerOpenHistory.getInstance().isOpenEnoughTimesInPeriod(idleDayCount);
    }

    @Override
    public Map<String, String> getSendInfo() {
        Map<String, String> sendMap = new HashMap<>();
        sendMap.put(CONSUMING_URL, new JSONObject(consumingMap).toString());
        sendMap.put(PROCESS_URL,  new JSONObject(processMap).toString());
        return sendMap;
    }

    void addTimeConsume(int timeConsume) {
        timeConsume += (int) consumingMap.get(ATTR_TIME_CONSUME);  // 加上之前的累计编辑时间
        consumingMap.put(ATTR_TIME_CONSUME, timeConsume);
    }

    void updateProcessMap(TemplateProcessInfo processInfo) {
        HashMap<String, Object> processMap = new HashMap<>();

        // 暂不支持模版制作过程的收集
        processMap.put(ATTR_PROCESS, StringUtils.EMPTY);

        processMap.put(ATTR_REPORT_TYPE, processInfo.getReportType());
        processMap.put(ATTR_CELL_COUNT, processInfo.getCellCount());
        processMap.put(ATTR_FLOAT_COUNT, processInfo.getFloatCount());
        processMap.put(ATTR_BLOCK_COUNT, processInfo.getBlockCount());
        processMap.put(ATTR_WIDGET_COUNT, processInfo.getWidgetCount());

        this.processMap = processMap;
    }

    int getIdleDayCount() {
        return this.idleDayCount;
    }
}