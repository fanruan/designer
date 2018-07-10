package com.fr.design.mainframe.templateinfo;

import com.fr.base.FRContext;
import com.fr.base.io.BaseBook;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.SiteCenterToken;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import com.fr.workspace.WorkContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector<T extends BaseBook> implements Serializable, XMLReadable, XMLWriter {
    static final long serialVersionUID = 2007L;
    private static final String FILE_NAME = "tpl.info";
    private static final String OBJECT_FILE_NAME = "tplInfo.ser";
    private static final int VALID_CELL_COUNT = 5;  // 有效报表模板的格子数
    private static final int VALID_WIDGET_COUNT = 5;  // 有效报表模板的控件数
    private static final int COMPLETE_DAY_COUNT = 15;  // 判断模板是否完成的天数
    private static final int ONE_THOUSAND = 1000;
    private static final String XML_DESIGNER_OPEN_DATE = "DesignerOpenDate";
    private static final String XML_TEMPLATE_INFO_LIST = "TemplateInfoList";
    private static final String XML_TEMPLATE_INFO = "TemplateInfo";
    private static final String XML_PROCESS_MAP = "processMap";
    private static final String XML_CONSUMING_MAP = "consumingMap";
    private static final String ATTR_DAY_COUNT = "day_count";
    private static final String ATTR_TEMPLATE_ID = "templateID";
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
    private static final String ATTR_VERSION = "version";
    private static final String ATTR_USERNAME = "username";
    private static final String JSON_CONSUMING_MAP = "jsonConsumingMap";
    private static final String JSON_PROCESS_MAP = "jsonProcessMap";
    private static TemplateInfoCollector instance;
    private Map<String, HashMap<String, Object>> templateInfoList;
    private String designerOpenDate;  //设计器最近一次打开日期


    @SuppressWarnings("unchecked")
    private TemplateInfoCollector() {
        templateInfoList = new HashMap<>();
        setDesignerOpenDate();
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    private static File getObjectInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), OBJECT_FILE_NAME));
    }

    public static TemplateInfoCollector getInstance() {
        if (instance == null) {
            instance = new TemplateInfoCollector();
            readXMLFile(instance, getInfoFile());
            // 兼容过渡。如果没有新文件，则从老文件读取数据。以后都是读写新的 xml 文件
            if (!getInfoFile().exists() && getObjectInfoFile().exists()) {
                try {
                    ObjectInputStream is = new ObjectInputStream(new FileInputStream(getObjectInfoFile()));
                    instance = (TemplateInfoCollector) is.readObject();
                } catch (Exception ex) {
                    // 什么也不做，instance 使用新值
                }
            }
        }
        return instance;
    }

    private static void readXMLFile(XMLReadable xmlReadable, File xmlFile) {
        if (xmlFile == null || !xmlFile.exists()) {
            return;
        }
        String charset = EncodeConstants.ENCODING_UTF_8;
        try {
            String fileContent = getFileContent(xmlFile);
            InputStream xmlInputStream = new ByteArrayInputStream(fileContent.getBytes(charset));
            InputStreamReader inputStreamReader = new InputStreamReader(xmlInputStream, charset);
            XMLableReader xmlReader = XMLableReader.createXMLableReader(inputStreamReader);

            if (xmlReader != null) {
                xmlReader.readXMLObject(xmlReadable);
            }
            xmlInputStream.close();
        } catch (FileNotFoundException e) {
            FRContext.getLogger().error(e.getMessage());
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        } catch (XMLStreamException e) {
            FRContext.getLogger().error(e.getMessage());
        }

    }

    private static String getFileContent(File xmlFile) throws FileNotFoundException, UnsupportedEncodingException {
        InputStream is = new FileInputStream(xmlFile);
        return IOUtils.inputStream2String(is);
    }

    public static void main(String[] args) {
        TemplateInfoCollector tic = TemplateInfoCollector.getInstance();
        tic.sendTemplateInfo();
    }

    /**
     * 把设计器最近打开日期设定为当前日期
     */
    private void setDesignerOpenDate() {
        designerOpenDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 判断今天是否第一次打开设计器
     */
    private boolean designerOpenFirstTime() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return !ComparatorUtils.equals(today, designerOpenDate);
    }

    private boolean shouldCollectInfo() {
        //只收集本地环境的
        if (!WorkContext.getCurrent().isLocal()) {
            return false;
        }
        return DesignerEnvManager.getEnvManager().isJoinProductImprove() && FRContext.isChineseEnv();
    }

    public void appendProcess(String log) {
        if (!shouldCollectInfo()) {
            return;
        }
        // 获取当前编辑的模板
        JTemplate jt = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        // 追加过程记录
        jt.appendProcess(log);
    }

    /**
     * 加载已经存储的模板过程
     */
    @SuppressWarnings("unchecked")
    public String loadProcess(T t) {
        HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfoList.get(t.getTemplateID()).get(XML_PROCESS_MAP);
        return (String) processMap.get(ATTR_PROCESS);
    }

    /**
     * 根据模板ID是否在收集列表中，判断是否需要收集当前模板的信息
     */
    public boolean inList(T t) {
        return templateInfoList.containsKey(t.getTemplateID());
    }

    /**
     * 将包含所有信息的对象保存到文件
     */
    private void saveInfo() {
        try {
            FileOutputStream out = new FileOutputStream(getInfoFile());
            XMLTools.writeOutputStreamXML(this, out);
        } catch (Exception ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage());
        }
    }

    /**
     * 更新 day_count：打开设计器却未编辑模板的连续日子
     */
    private void addDayCount() {
        if (designerOpenFirstTime()) {
            for (String key : templateInfoList.keySet()) {
                HashMap<String, Object> templateInfo = templateInfoList.get(key);
                int dayCount = (int) templateInfo.get(ATTR_DAY_COUNT) + 1;
                templateInfo.put(ATTR_DAY_COUNT, dayCount);
            }
            setDesignerOpenDate();
        }
    }

    /**
     * 收集模板信息。如果之前没有记录，则新增；如果已有记录，则更新。
     * 同时将最新数据保存到文件中。
     */
    @SuppressWarnings("unchecked")
    public void collectInfo(T t, JTemplate jt, long openTime, long saveTime) {
        if (!shouldCollectInfo()) {
            return;
        }

        HashMap<String, Object> templateInfo;

        long timeConsume = ((saveTime - openTime) / ONE_THOUSAND);  // 制作模板耗时（单位：s）
        String templateID = t.getTemplateID();

        if (inList(t)) { // 已有记录
            templateInfo = templateInfoList.get(templateID);
            // 更新 conusmingMap
            HashMap<String, Object> consumingMap = (HashMap<String, Object>) templateInfo.get(XML_CONSUMING_MAP);
            timeConsume += (long) consumingMap.get(ATTR_TIME_CONSUME);  // 加上之前的累计编辑时间
            consumingMap.put(ATTR_TIME_CONSUME, timeConsume);
        } else {  // 新增
            templateInfo = new HashMap<>();
            templateInfo.put(XML_CONSUMING_MAP, getNewConsumingMap(templateID, openTime, timeConsume));
        }

        // 直接覆盖 processMap
        templateInfo.put(XML_PROCESS_MAP, getProcessMap(templateID, jt));

        // 保存模板时，让 day_count 归零
        templateInfo.put(ATTR_DAY_COUNT, 0);

        templateInfoList.put(templateID, templateInfo);

        saveInfo();  // 每次更新之后，都同步到暂存文件中
    }

    private HashMap<String, Object> getNewConsumingMap(String templateID, long openTime, long timeConsume) {
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
        consumingMap.put(ATTR_CREATE_TIME, createTime);
        consumingMap.put(ATTR_TIME_CONSUME, timeConsume);
        consumingMap.put(ATTR_JAR_TIME, jarTime);
        consumingMap.put(ATTR_VERSION, version);

        return consumingMap;
    }

    private HashMap<String, Object> getProcessMap(String templateID, JTemplate jt) {
        HashMap<String, Object> processMap = new HashMap<>();

        processMap.put(ATTR_TEMPLATE_ID, templateID);
        processMap.put(ATTR_PROCESS, jt.getProcess());

        TemplateProcessInfo info = jt.getProcessInfo();
        processMap.put(ATTR_REPORT_TYPE, info.getReportType());
        processMap.put(ATTR_CELL_COUNT, info.getCellCount());
        processMap.put(ATTR_FLOAT_COUNT, info.getFloatCount());
        processMap.put(ATTR_BLOCK_COUNT, info.getBlockCount());
        processMap.put(ATTR_WIDGET_COUNT, info.getWidgetCount());

        return processMap;
    }

    /**
     * 发送本地模板信息到服务器
     */
    public void sendTemplateInfo() {
        addDayCount();
        String consumingUrl = CloudCenter.getInstance().acquireUrlByKind("tempinfo.consuming") + "/single";
        String processUrl = CloudCenter.getInstance().acquireUrlByKind("tempinfo.process") + "/single";
        ArrayList<HashMap<String, String>> completeTemplatesInfo = getCompleteTemplatesInfo();
        for (HashMap<String, String> templateInfo : completeTemplatesInfo) {
            String jsonConsumingMap = templateInfo.get(JSON_CONSUMING_MAP);
            String jsonProcessMap = templateInfo.get(JSON_PROCESS_MAP);
            if (sendSingleTemplateInfo(consumingUrl, jsonConsumingMap) && sendSingleTemplateInfo(processUrl, jsonProcessMap)) {
                // 清空记录
                removeFromTemplateInfoList(templateInfo.get(ATTR_TEMPLATE_ID));
            }
        }
        saveInfo();
    }

    private boolean sendSingleTemplateInfo(String url, String content) {
        HashMap<String, String> para = new HashMap<>();
        para.put("token", SiteCenterToken.generateToken());
        para.put("content", content);
        HttpClient httpClient = new HttpClient(url, para, true);
        httpClient.setTimeout(5000);
        httpClient.asGet();

        if (!httpClient.isServerAlive()) {
            return false;
        }

        String res = httpClient.getResponseText();
        boolean success;
        try {
            success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }

    /**
     * 返回已完成的模板信息
     */
    @SuppressWarnings("unchecked")
    private ArrayList<HashMap<String, String>> getCompleteTemplatesInfo() {
        ArrayList<HashMap<String, String>> completeTemplatesInfo = new ArrayList<>();
        ArrayList<String> testTemplateKeys = new ArrayList<>();  // 保存测试模板的key
        for (String key : templateInfoList.keySet()) {
            HashMap<String, Object> templateInfo = templateInfoList.get(key);
            if ((int) templateInfo.get(ATTR_DAY_COUNT) <= COMPLETE_DAY_COUNT) {  // 未完成模板
                continue;
            }
            if (isTestTemplate(templateInfo)) {
                testTemplateKeys.add(key);
                continue;
            }
            HashMap<String, Object> consumingMap = (HashMap<String, Object>) templateInfo.get(XML_CONSUMING_MAP);
            HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfo.get(XML_PROCESS_MAP);
            String jsonConsumingMap = new JSONObject(consumingMap).toString();
            String jsonProcessMap = new JSONObject(processMap).toString();
            HashMap<String, String> jsonTemplateInfo = new HashMap<>();
            jsonTemplateInfo.put(JSON_CONSUMING_MAP, jsonConsumingMap);
            jsonTemplateInfo.put(JSON_PROCESS_MAP, jsonProcessMap);
            jsonTemplateInfo.put(ATTR_TEMPLATE_ID, key);
            completeTemplatesInfo.add(jsonTemplateInfo);
        }
        // 删除测试模板
        for (String key : testTemplateKeys) {
            removeFromTemplateInfoList(key);
        }
        return completeTemplatesInfo;
    }

    private void removeFromTemplateInfoList(String key) {
        templateInfoList.remove(key);
    }

    @SuppressWarnings("unchecked")
    private boolean isTestTemplate(HashMap<String, Object> templateInfo) {
        HashMap<String, Object> processMap = (HashMap<String, Object>) templateInfo.get(XML_PROCESS_MAP);
        int reportType = (int) processMap.get(ATTR_REPORT_TYPE);
        int cellCount = (int) processMap.get(ATTR_CELL_COUNT);
        int floatCount = (int) processMap.get(ATTR_FLOAT_COUNT);
        int blockCount = (int) processMap.get(ATTR_BLOCK_COUNT);
        int widgetCount = (int) processMap.get(ATTR_WIDGET_COUNT);
        boolean isTestTemplate = false;
        if (reportType == 0) {  // 普通报表
            isTestTemplate = cellCount <= VALID_CELL_COUNT && floatCount <= 1 && widgetCount <= VALID_WIDGET_COUNT;
        } else if (reportType == 1) {  // 聚合报表
            isTestTemplate = blockCount <= 1 && widgetCount <= VALID_WIDGET_COUNT;
        } else {  // 表单(reportType == 2)
            isTestTemplate = widgetCount <= 1;
        }
        return isTestTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            try {
                String name = reader.getTagName();
                if (XML_DESIGNER_OPEN_DATE.equals(name)) {
                    this.designerOpenDate = reader.getElementValue();
                } else if (XML_TEMPLATE_INFO_LIST.equals(name)) {
                    readTemplateInfoList(reader);
                }
            } catch (Exception ex) {
                // 什么也不做，使用默认值
            }
        }
    }

    private void readTemplateInfoList(XMLableReader reader) {
        reader.readXMLObject(new XMLReadable() {
            public void readXML(XMLableReader reader) {
                if (XML_TEMPLATE_INFO.equals(reader.getTagName())) {
                    TemplateInfo templateInfo = new TemplateInfo();
                    reader.readXMLObject(templateInfo);
                    templateInfoList.put(templateInfo.getTemplateID(), templateInfo.getTemplateInfo());
                }
            }
        });
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("TplInfo");

        writer.startTAG(XML_DESIGNER_OPEN_DATE);
        writer.textNode(designerOpenDate);
        writer.end();

        writeTemplateInfoList(writer);

        writer.end();
    }

    private void writeTemplateInfoList(XMLPrintWriter writer) {
        //启停
        writer.startTAG(XML_TEMPLATE_INFO_LIST);
        for (String templateID : templateInfoList.keySet()) {
            new TemplateInfo(templateInfoList.get(templateID)).writeXML(writer);
        }
        writer.end();
    }

    private class TemplateInfo implements XMLReadable, XMLWriter {

        private int dayCount;
        private String templateID;
        private HashMap<String, Object> processMap = new HashMap<>();
        private HashMap<String, Object> consumingMap = new HashMap<>();

        @SuppressWarnings("unchecked")
        public TemplateInfo(HashMap<String, Object> templateInfo) {
            this.dayCount = (int) templateInfo.get(ATTR_DAY_COUNT);
            this.processMap = (HashMap<String, Object>) templateInfo.get(XML_PROCESS_MAP);
            this.consumingMap = (HashMap<String, Object>) templateInfo.get(XML_CONSUMING_MAP);
            this.templateID = (String) processMap.get(ATTR_TEMPLATE_ID);
        }

        public TemplateInfo() {
        }

        public String getTemplateID() {
            return templateID;
        }

        public HashMap<String, Object> getTemplateInfo() {
            HashMap<String, Object> templateInfo = new HashMap<>();
            templateInfo.put(XML_PROCESS_MAP, processMap);
            templateInfo.put(XML_CONSUMING_MAP, consumingMap);
            templateInfo.put(ATTR_DAY_COUNT, dayCount);
            return templateInfo;
        }

        public void writeXML(XMLPrintWriter writer) {
            writer.startTAG(XML_TEMPLATE_INFO);
            if (StringUtils.isNotEmpty(templateID)) {
                writer.attr(ATTR_TEMPLATE_ID, this.templateID);
            }
            if (dayCount >= 0) {
                writer.attr(ATTR_DAY_COUNT, this.dayCount);
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
            writer.attr(ATTR_TIME_CONSUME, (long) consumingMap.get(ATTR_TIME_CONSUME));
            writer.attr(ATTR_VERSION, (String) consumingMap.get(ATTR_VERSION));
            writer.attr(ATTR_USERNAME, (String) consumingMap.get(ATTR_USERNAME));
            writer.end();
        }

        public void readXML(XMLableReader reader) {
            if (!reader.isChildNode()) {
                dayCount = reader.getAttrAsInt(ATTR_DAY_COUNT, 0);
                templateID = reader.getAttrAsString(ATTR_TEMPLATE_ID, StringUtils.EMPTY);
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
                        consumingMap.put(ATTR_UUID, reader.getAttrAsString(ATTR_UUID, StringUtils.EMPTY));
                        consumingMap.put(ATTR_TIME_CONSUME, reader.getAttrAsLong(ATTR_TIME_CONSUME, 0));
                        consumingMap.put(ATTR_VERSION, reader.getAttrAsString(ATTR_VERSION, "8.0"));
                        consumingMap.put(ATTR_USERNAME, reader.getAttrAsString(ATTR_USERNAME, StringUtils.EMPTY));
                    }
                } catch (Exception ex) {
                    // 什么也不做，使用默认值
                }
            }
        }

    }
}
