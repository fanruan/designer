package com.fr.design.mainframe.template.info;

import com.fr.design.mainframe.burying.point.AbstractPointCollector;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.io.File;

/**
 * 做模板的过程和耗时收集，辅助类
 * Created by plough on 2017/2/21.
 */
public class TemplateInfoCollector extends AbstractPointCollector<TemplateInfo> {
    private static final String XML_TAG = "TplInfo";
    private static final String XML_TEMPLATE_INFO_LIST = "TemplateInfoList";
    private static final String XML_FILE_NAME = "tpl.info";
    private static TemplateInfoCollector instance;
    private DesignerOpenHistory designerOpenHistory;

    private TemplateInfoCollector() {
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
        return StringUtils.isNotEmpty(templateID) && pointInfoMap.containsKey(templateID);
    }

    /**
     * 收集模板信息。如果之前没有记录，则新增；如果已有记录，则更新。
     * 同时将最新数据保存到文件中。
     *
     * @param templateID  模版id
     * @param originID    模版的原始id，仅对另存为的模版有效，对于非另存为的模版，值总是为空
     * @param processInfo 包含模版的一些基本信息（如模版类型、包含控件数量等）
     * @param timeConsume 本次制作耗时，单位为 s
     */
    @Override
    public void collectInfo(String templateID, String originID, TemplateProcessInfo processInfo, int timeConsume) {
        if (!shouldCollectInfo()) {
            return;
        }

        TemplateInfo templateInfo;
        if (this.contains(templateID)) {
            templateInfo = pointInfoMap.get(templateID);
        } else {
            int originTime = this.contains(originID) ? pointInfoMap.get(originID).getTimeConsume() : 0;
            templateInfo = TemplateInfo.newInstance(templateID, originID, originTime);
            pointInfoMap.put(templateID, templateInfo);
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
     * 获取缓存文件存放路径
     */
    @Override
    protected File getInfoFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), XML_FILE_NAME));
    }

    TemplateInfo getOrCreateTemplateInfoByID(String templateID) {
        if (pointInfoMap.containsKey(templateID)) {
            return pointInfoMap.get(templateID);
        }
        TemplateInfo templateInfo = TemplateInfo.newInstance(templateID);
        pointInfoMap.put(templateID, templateInfo);
        return templateInfo;
    }

    /**
     * 更新 day_count：打开设计器却未编辑模板的连续日子
     */
    @Override
    protected void addIdleDayCount() {
        // 判断今天是否第一次打开设计器，为了防止同一天内，多次 addIdleDayCount
        if (designerOpenHistory.hasOpenedToday()) {
            return;
        }
        for (TemplateInfo templateInfo : pointInfoMap.values()) {
            templateInfo.addIdleDayCountByOne();
        }
        designerOpenHistory.update();
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            try {
                String name = reader.getTagName();
                if (DesignerOpenHistory.XML_TAG.equals(name)) {
                    if (designerOpenHistory == null) {
                        designerOpenHistory = DesignerOpenHistory.getInstance();
                    }
                    reader.readXMLObject(designerOpenHistory);
                } else if (TemplateInfo.XML_TAG.equals(name)) {
                    TemplateInfo templateInfo = TemplateInfo.newInstanceByRead(reader);
                    pointInfoMap.put(templateInfo.getTemplateID(), templateInfo);
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
        for (TemplateInfo templateInfo : pointInfoMap.values()) {
            templateInfo.writeXML(writer);
        }
        writer.end();

        writer.end();
    }
}
