package com.fr.design.mainframe.template.info;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
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
public class TemplateInfoCollector implements Serializable, XMLReadable, XMLWriter {
    private static final String XML_TAG = "TplInfo";
    private static final String XML_DESIGNER_OPEN_DATE = "DesignerOpenDate";
    private static final String XML_TEMPLATE_INFO_LIST = "TemplateInfoList";

    static final long serialVersionUID = 2007L;
    private static final String XML_FILE_NAME = "tpl.info";
    private static final String OBJECT_FILE_NAME = "tplInfo.ser";

    private static final int ONE_THOUSAND = 1000;

    private static TemplateInfoCollector instance;
    private Map<String, TemplateInfo> templateInfoMap;
    private String designerOpenDate;  //设计器最近一次打开日期


    @SuppressWarnings("unchecked")
    private TemplateInfoCollector() {
        templateInfoMap = new HashMap<>();
        setDesignerOpenDate();
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile(String fileName) {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), fileName));
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        return getInfoFile(XML_FILE_NAME);
    }

    public static TemplateInfoCollector getInstance() {
        if (instance == null) {
            instance = new TemplateInfoCollector();

            File xmlFile = getInfoFile();
            File objFile = getInfoFile(OBJECT_FILE_NAME);
            if (xmlFile.exists()) {
                readXMLFile(instance, xmlFile);
            } else if (objFile.exists()) {
                readFromObjectFile(objFile);
            }
        }
        return instance;
    }

    private static void readFromObjectFile(File objFile) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(objFile));
            instance = (TemplateInfoCollector) is.readObject();
        } catch (Exception ex) {
            // 什么也不做，instance 使用新值
        }
    }

    /**
     * 根据模板ID是否在收集列表中，判断是否需要收集当前模板的信息
     */
    public boolean contains(String templateID) {
        return StringUtils.isNotEmpty(templateID) && templateInfoMap.containsKey(templateID);
    }



    /**
     * 收集模板信息。如果之前没有记录，则新增；如果已有记录，则更新。
     * 同时将最新数据保存到文件中。
     */
    @SuppressWarnings("unchecked")
    public void collectInfo(String templateID, TemplateProcessInfo processInfo, long openTime, long saveTime) {
        if (!shouldCollectInfo()) {
            return;
        }

        long timeConsume = ((saveTime - openTime) / ONE_THOUSAND);  // 制作模板耗时（单位：s）


        TemplateInfo templateInfo;

        if (templateInfoMap.containsKey(templateID)) { // 已有记录
            templateInfo = templateInfoMap.get(templateID);
            templateInfo.addTimeConsume(timeConsume);
        } else {  // 新增
            templateInfo = TemplateInfo.newInstance(templateID, timeConsume);
        }

        templateInfo.updateProcessMap(processInfo);

        // 保存模板时，让 day_count 归零
        templateInfo.setIdleDayCount(0);

        templateInfoMap.put(templateID, templateInfo);

        saveInfo();  // 每次更新之后，都同步到暂存文件中
    }

    /**
     * 发送本地模板信息到服务器，并清空已发送模版的本地记录
     */
    public void sendTemplateInfo() {
        addDayCount();

        removeTestTemplates();

        for (String key : templateInfoMap.keySet()) {
            TemplateInfo templateInfo = templateInfoMap.get(key);
            if (templateInfo.isReadyForSend()) {
                if (SendHelper.sendTemplateInfo(templateInfo)) {
                    // 清空记录
                    removeFromTemplateInfoList(templateInfo.getTemplateID());
                }
            }
        }

        saveInfo();
    }

    private static void readXMLFile(XMLReadable xmlReadable, File xmlFile) {
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
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (XMLStreamException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

    }

    private static String getFileContent(File xmlFile) throws FileNotFoundException, UnsupportedEncodingException {
        InputStream is = new FileInputStream(xmlFile);
        return IOUtils.inputStream2String(is);
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
            for (TemplateInfo templateInfo : templateInfoMap.values()) {
                templateInfo.addIdleDayCountByOne();
            }
            setDesignerOpenDate();
        }
    }


    private void removeTestTemplates() {
        // 删除所有已完成的测试模版
        ArrayList<String> testTemplateKeys = new ArrayList<>();  // 保存测试模板的key
        for (String key : templateInfoMap.keySet()) {
            if (templateInfoMap.get(key).isTestTemplate()) {
                testTemplateKeys.add(key);
            }
        }
        // 删除测试模板
        for (String key : testTemplateKeys) {
            removeFromTemplateInfoList(key);
        }
    }

    private void removeFromTemplateInfoList(String key) {
        templateInfoMap.remove(key);
    }

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
                if (TemplateInfo.XML_TAG.equals(reader.getTagName())) {
                    TemplateInfo templateInfo = TemplateInfo.newInstance(reader);
                    templateInfoMap.put(templateInfo.getTemplateID(), templateInfo);
                }
            }
        });
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        writer.startTAG(XML_DESIGNER_OPEN_DATE);
        writer.textNode(designerOpenDate);
        writer.end();

        writer.startTAG(XML_TEMPLATE_INFO_LIST);
        for (TemplateInfo templateInfo : templateInfoMap.values()) {
            templateInfo.writeXML(writer);
        }
        writer.end();

        writer.end();
    }
}
