package com.fr.design.mainframe.template.info;

import com.fr.base.FRContext;
import com.fr.base.io.XMLReadHelper;
import com.fr.design.DesignerEnvManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector implements XMLReadable, XMLWriter {
    private static final String XML_TAG = "TplInfo";
    private static final String XML_TEMPLATE_INFO_LIST = "TemplateInfoList";
    private static final String XML_FILE_NAME = "tpl.info";
    private static TemplateInfoCollector instance;
    private static final int MAX_SIZE = 512 * 1024 * 1024;
    private Map<String, TemplateInfo> templateInfoMap;
    private DesignerOpenHistory designerOpenHistory;

    private TemplateInfoCollector() {
        init();
    }

    private void init() {
        templateInfoMap = new HashMap<>();
        designerOpenHistory = DesignerOpenHistory.getInstance();

        loadFromFile();
    }

    public static TemplateInfoCollector getInstance() {
        if (instance == null) {
            instance = new TemplateInfoCollector();
        }
        return instance;
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
     * @param templateID 模版id
     * @param originID 模版的原始id，仅对另存为的模版有效，对于非另存为的模版，值总是为空
     * @param processInfo 包含模版的一些基本信息（如模版类型、包含控件数量等）
     * @param timeConsume 本次制作耗时，单位为 s
     */
    public void collectInfo(String templateID, String originID, TemplateProcessInfo processInfo, int timeConsume) {
        if (!shouldCollectInfo()) {
            return;
        }

        TemplateInfo templateInfo;
        if (this.contains(templateID)) {
            templateInfo = templateInfoMap.get(templateID);
        } else {
            int originTime = this.contains(originID) ? templateInfoMap.get(originID).getTimeConsume() : 0;
            templateInfo = TemplateInfo.newInstance(templateID, originID, originTime);
            templateInfoMap.put(templateID, templateInfo);
        }

        // 收集制作耗时
        templateInfo.addTimeConsume(timeConsume);
        // 收集模版基本信息
        templateInfo.updateProcessMap(processInfo);
        // 刷新闲置日计数器
        templateInfo.resetIdleDayCount();

        // 每次更新之后，都同步到暂存文件中
        saveInfo();
    }

    /**
     * 发送本地模板信息到服务器，并清空已发送模版的本地记录
     */
    public void sendTemplateInfo() {
        // 每次启动设计器后，都会执行这个函数（被 InformationCollector 的 collectStartTime 调用）
        addIdleDayCount();

        removeTestTemplates();

        List<String> removeLaterList = new ArrayList<>();

        for (String key : templateInfoMap.keySet()) {
            TemplateInfo templateInfo = templateInfoMap.get(key);
            if (templateInfo.isReadyForSend()) {
                if (SendHelper.sendTemplateInfo(templateInfo)) {
                    removeLaterList.add(key);
                }
            }
        }

        // 清空记录
        for (String key : removeLaterList) {
            removeFromTemplateInfoList(key);
        }

        saveInfo();
    }

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), XML_FILE_NAME));
    }

    void loadFromFile() {
        if (!getInfoFile().exists()) {
            return;
        }

        XMLableReader reader = null;
        try (InputStream in = new FileInputStream(getInfoFile())) {
            // XMLableReader 还是应该考虑实现 Closable 接口的，这样就能使用 try-with 语句了
            reader = XMLReadHelper.createXMLableReader(in, XMLPrintWriter.XML_ENCODER);
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

    TemplateInfo getOrCreateTemplateInfoByID(String templateID) {
        if (templateInfoMap.containsKey(templateID)) {
            return templateInfoMap.get(templateID);
        }
        TemplateInfo templateInfo = TemplateInfo.newInstance(templateID);
        templateInfoMap.put(templateID, templateInfo);
        return templateInfo;
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

    /**
     * 更新 day_count：打开设计器却未编辑模板的连续日子
     */
    private void addIdleDayCount() {
        // 判断今天是否第一次打开设计器，为了防止同一天内，多次 addIdleDayCount
        if (designerOpenHistory.hasOpenedToday()) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfoMap.values()) {
            templateInfo.addIdleDayCountByOne();
        }
        designerOpenHistory.update();
    }

    // 删除所有已完成的测试模版
    private void removeTestTemplates() {
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
                if (DesignerOpenHistory.XML_TAG.equals(name)) {
                    reader.readXMLObject(designerOpenHistory);
                } else if (TemplateInfo.XML_TAG.equals(name)) {
                    TemplateInfo templateInfo = TemplateInfo.newInstanceByRead(reader);
                    templateInfoMap.put(templateInfo.getTemplateID(), templateInfo);
                }
            } catch (Exception ex) {
                // 什么也不做，使用默认值
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);

        designerOpenHistory.writeXML(writer);

        writer.startTAG(XML_TEMPLATE_INFO_LIST);
        for (TemplateInfo templateInfo : templateInfoMap.values()) {
            templateInfo.writeXML(writer);
        }
        writer.end();

        writer.end();
    }
}
