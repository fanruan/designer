/**
 *
 */
package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.errorinfo.ErrorInfoUploader;
import com.fr.design.mainframe.messagecollect.impl.FocusPointMessageUploader;
import com.fr.design.mainframe.template.info.TemplateInfoCollector;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.DesUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
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
import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author neil
 * @date: 2015-4-8-下午5:11:46
 */
public class InformationCollector implements XMLReadable, XMLWriter {

    /**
     * 24小时上传一次
     */
    private static final long DELTA = 24 * 3600 * 1000L;
    private static final long SEND_DELAY = 300 * 1000L;
    private static final int BYTE_TO_MB = 1024 * 1024;
    private static final String FILE_NAME = "fr.info";
    private static final String XML_START_STOP_LIST = "StartStopList";
    private static final String XML_START_STOP = "StartStop";
    private static final String XML_LAST_TIME = "LastTime";
    private static final String ATTR_START = "start";
    private static final String ATTR_STOP = "stop";
    private static final String XML_JAR = "JarInfo";
    private static final String XML_VERSION = "Version";
    private static final String XML_USERNAME = "Username";
    private static final String XML_UUID = "UUID";
    private static final String XML_KEY = "ActiveKey";
    private static final String XML_OS = "OS";
    private static final String XML_ARCH = "arch";
    private static final String XML_AVAILABLE_PROCESSORS = "cpu";
    private static final String XML_PHYSICAL_MEMORY = "systemMemory";

    private static InformationCollector collector;

    /**
     * 启动时间与关闭时间列表
     */
    private List<StartStopTime> startStop = new ArrayList<StartStopTime>();

    /**
     * 上一次的发送时间
     */
    private String lastTime;
    private StartStopTime current = new StartStopTime();

    public static InformationCollector getInstance() {
        if (collector == null) {
            collector = new InformationCollector();

            readEncodeXMLFile(collector, collector.getInfoFile());
        }

        return collector;
    }

    private static void readEncodeXMLFile(XMLReadable xmlReadable, File xmlFile) {
        if (xmlFile == null || !xmlFile.exists()) {
            return;
        }
        String charset = EncodeConstants.ENCODING_UTF_8;
        try {
            String decodeContent = getDecodeFileContent(xmlFile);
            InputStream xmlInputStream = new ByteArrayInputStream(decodeContent.getBytes(charset));
            InputStreamReader inputStreamReader = new InputStreamReader(xmlInputStream, charset);

            XMLableReader xmlReader = XMLableReader.createXMLableReader(inputStreamReader);

            if (xmlReader != null) {
                xmlReader.readXMLObject(xmlReadable);
            }
            xmlInputStream.close();
        } catch (FileNotFoundException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (XMLStreamException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

    }

    private static String getDecodeFileContent(File xmlFile) throws FileNotFoundException, UnsupportedEncodingException {
        InputStream encodeInputStream = new FileInputStream(xmlFile);
        String encodeContent = IOUtils.inputStream2String(encodeInputStream);
        return DesUtils.getDecString(encodeContent);
    }

    private long getLastTimeMillis() {
        if (StringUtils.isEmpty(this.lastTime)) {
            return 0;
        }

        try {
            return Objects.requireNonNull(DateUtils.string2Date(this.lastTime, true)).getTime();
        } catch (Exception e) {
            return -1;
        }

    }

    private JSONObject getJSONContentAsByte() {
        JSONObject content = new JSONObject();

        JSONArray startStopArray = new JSONArray();
        for (StartStopTime startStopTime : startStop) {
            JSONObject jo = new JSONObject();
            jo.put(ATTR_START, startStopTime.getStartDate());
            jo.put(ATTR_STOP, startStopTime.getStopDate());
            startStopArray.put(jo);
        }
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        content.put(XML_START_STOP, startStopArray);
        content.put(XML_UUID, envManager.getUUID());
        content.put(XML_JAR, GeneralUtils.readBuildNO());
        content.put(XML_VERSION, ProductConstants.RELEASE_VERSION);
        content.put(XML_USERNAME, MarketConfig.getInstance().getBbsUsername());
        content.put(XML_KEY, envManager.getActivationKey());
        content.put(XML_OS, System.getProperty("os.name"));
        content.put(XML_ARCH, System.getProperty("os.arch"));
        content.put(XML_AVAILABLE_PROCESSORS, Runtime.getRuntime().availableProcessors());
        content.put(XML_PHYSICAL_MEMORY, getTotalPhysicalMemorySize());
        return content;
    }

    /**
     * 获取物理内存，单位GB
     *
     * @return 物理内存
     */
    private static long getTotalPhysicalMemorySize() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return bean.getTotalPhysicalMemorySize() / BYTE_TO_MB;
    }


    private void sendUserInfo() {
        long currentTime = System.currentTimeMillis();
        long lastTime = getLastTimeMillis();

        if (currentTime - lastTime <= DELTA) {
            return;
        }
        JSONObject content = getJSONContentAsByte();
        String url = CloudCenter.getInstance().acquireUrlByKind("user.info.v10");
        boolean success = false;
        try {
            HashMap<String, Object> para = new HashMap<>();
            para.put("token", SiteCenterToken.generateToken());
            para.put("content", content);
            String res = HttpToolbox.post(url, para);
            success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        if (success) {
            this.reset();
        }
    }

    /**
     * 收集开始使用时间，发送信息
     */
    public void collectStartTime() {
        this.current.setStartDate(dateToString());

        sendUserInfoInOtherThread();
    }

    private void sendUserInfoInOtherThread() {
        if (!DesignerEnvManager.getEnvManager().isJoinProductImprove() || !FRContext.isChineseEnv()) {
            return;
        }

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor(new NamedThreadFactory("InformationCollector"));
        service.schedule(new Runnable() {
            @Override
            public void run() {
                sendUserInfo();
                FocusPointMessageUploader.getInstance().sendToCloudCenter();
                TemplateInfoCollector.getInstance().sendTemplateInfo();
                ErrorInfoUploader.getInstance().sendErrorInfo();
            }
        }, SEND_DELAY, TimeUnit.MILLISECONDS);
        service.shutdown();
    }

    /**
     * 收集结束使用时间
     */
    public void collectStopTime() {
        this.current.setStopDate(dateToString());
    }

    private String dateToString() {
        DateFormat df = FRContext.getDefaultValues().getDateTimeFormat();
        return df.format(new Date());
    }

    private void reset() {
        this.startStop.clear();
        this.lastTime = dateToString();
    }

    private File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    /**
     * 保存xml文件
     */
    public void saveXMLFile() {
        File xmlFile = this.getInfoFile();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLTools.writeOutputStreamXML(this, out);
            out.flush();
            out.close();
            String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
            String encodeCotent = DesUtils.getEncString(fileContent);
            writeEncodeContentToFile(encodeCotent, xmlFile);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }


    /**
     * 将文件内容写到输出流中
     */
    private static void writeEncodeContentToFile(String fileContent, File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(fileContent);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        startStop.add(current);
        writer.startTAG("Info");
        //启停信息
        writeStartStopList(writer);
        //上一次更新的时间
        writeTag(XML_LAST_TIME, this.lastTime, writer);

        writer.end();
    }

    private void writeStartStopList(XMLPrintWriter writer) {
        //启停
        writer.startTAG(XML_START_STOP_LIST);
        for (StartStopTime startStopTime : startStop) {
            startStopTime.writeXML(writer);
        }
        writer.end();
    }

    private void writeTag(String tag, String content, XMLPrintWriter writer) {
        if (StringUtils.isEmpty(content)) {
            return;
        }

        writer.startTAG(tag);
        writer.textNode(content);
        writer.end();
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String name = reader.getTagName();
            if (XML_START_STOP_LIST.equals(name)) {
                readStartStopList(reader);
            } else if (XML_LAST_TIME.equals(name)) {
                readLastTime(reader);
            }
        }
    }

    private void readLastTime(XMLableReader reader) {
        String tmpVal;
        if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
            this.lastTime = tmpVal;
        }
    }

    private void readStartStopList(XMLableReader reader) {
        startStop.clear();

        reader.readXMLObject(new XMLReadable() {
            @Override
            public void readXML(XMLableReader reader) {
                if (XML_START_STOP.equals(reader.getTagName())) {
                    StartStopTime startStopTime = new StartStopTime();
                    reader.readXMLObject(startStopTime);
                    startStop.add(startStopTime);
                }
            }
        });
    }

    private class StartStopTime implements XMLReadable, XMLWriter {

        private String startDate;
        private String stopDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStopDate() {
            return stopDate;
        }

        public void setStopDate(String endDate) {
            this.stopDate = endDate;
        }

        @Override
        public void writeXML(XMLPrintWriter writer) {
            writer.startTAG(XML_START_STOP);
            if (StringUtils.isNotEmpty(startDate)) {
                writer.attr(ATTR_START, this.startDate);
            }
            if (StringUtils.isNotEmpty(stopDate)) {
                writer.attr(ATTR_STOP, this.stopDate);
            }
            writer.end();
        }

        @Override
        public void readXML(XMLableReader reader) {
            this.startDate = reader.getAttrAsString(ATTR_START, StringUtils.EMPTY);
            this.stopDate = reader.getAttrAsString(ATTR_STOP, StringUtils.EMPTY);
        }

    }
}