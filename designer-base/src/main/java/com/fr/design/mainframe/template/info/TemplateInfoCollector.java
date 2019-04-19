package com.fr.design.mainframe.template.info;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
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
import com.fr.workspace.WorkContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector implements XMLReadable, XMLWriter {
    private static final String XML_TAG = "TplInfo";
    private static final String XML_DESIGNER_OPEN_DATE = "DesignerOpenDate";
    private static final String XML_TEMPLATE_INFO_LIST = "TemplateInfoList";
    private static final String XML_FILE_NAME = "tpl.info";
    private static TemplateInfoCollector instance;
    private Map<String, TemplateInfo> templateInfoMap;
    private String designerOpenDate;  //设计器最近一次打开日期

    private TemplateInfoCollector() {
        init();
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
        } else if (!this.contains(originID)) {
            templateInfo = TemplateInfo.newInstance(templateID);
            templateInfoMap.put(templateID, templateInfo);
        } else {
            int originTime = templateInfoMap.get(originID).getTimeConsume();
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
        addIdleDayCount();

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

    /**
     * 获取缓存文件存放路径
     */
    private static File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), XML_FILE_NAME));
    }

    private void init() {
        templateInfoMap = new HashMap<>();
        setDesignerOpenDate();

        loadFromFile();
    }

    void loadFromFile() {
        if (!getInfoFile().exists()) {
            return;
        }
        try {
            XMLableReader xmlReader = XMLableReader.createXMLableReader(new FileReader(getInfoFile()));
            xmlReader.readXMLObject(this);
        } catch (XMLStreamException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            // do nothing
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

    /**
     * 把设计器最近打开日期设定为当前日期
     */
    private void setDesignerOpenDate() {
        designerOpenDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 判断今天是否第一次打开设计器，为了防止同一天内，多次 addIdleDayCount
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
    private void addIdleDayCount() {
        if (designerOpenFirstTime()) {
            for (TemplateInfo templateInfo : templateInfoMap.values()) {
                templateInfo.addIdleDayCountByOne();
            }
            setDesignerOpenDate();
        }
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
                if (XML_DESIGNER_OPEN_DATE.equals(name)) {
                    this.designerOpenDate = reader.getElementValue();
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
