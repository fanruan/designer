package com.fr.design.actions.help.alphafine;

import com.fr.license.function.VT4FR;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import javax.swing.*;

/**
 * Created by XiaXiang on 2017/4/5.
 */
public class AlphaFineConfigManager implements XMLable {

    private static AlphaFineConfigManager alphaFineConfigManager = new AlphaFineConfigManager();
    /**
     * 是否开启alphafine
     */
    private boolean enabled = true;
    /**
     * 是否联网搜索
     */
    private boolean searchOnLine = true;

    /**
     * 搜索范围
     */
    /**
     * 快捷键设置
     */
    private String shortcuts;
    /**
     * 猜您需要
     */
    private boolean containRecommend = true;
    /**
     * 设置
     */
    private boolean containAction = true;
    /**
     * 帮助文档
     */
    private boolean containDocument = true;
    /**
     * 模板
     */
    private boolean containTemplate = true;
    /**
     * 模板内容
     */
    private boolean containFileContent;
    /**
     * 应用中心
     */
    private boolean containPlugin = true;
    /**
    * 分词搜索
    */
    private boolean needSegmentationCheckbox = true;
    /**
    * 智能客服
    */
    private boolean needIntelligentCustomerService = true;
    /**
     * 快捷键
     */
    private KeyStroke shortCutKeyStore;
    /**
     * 是否提醒
     */
    private boolean needRemind = true;
    /**
     * 直接操作菜单次数
     */
    private int operateCount;

    public static AlphaFineConfigManager getInstance() {
        return alphaFineConfigManager;
    }

    public static boolean isALPHALicAvailable() {
    
        return VT4FR.AlphaFine.isSupport();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AlphaFineConfigManager manager = (AlphaFineConfigManager) super.clone();
        return manager;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            this.setEnabled(reader.getAttrAsBoolean("enabled", true));
            this.setSearchOnLine(reader.getAttrAsBoolean("searchOnline", true));
            this.setContainPlugin(reader.getAttrAsBoolean("containDocument", true));
            this.setContainDocument(reader.getAttrAsBoolean("containDocument", true));
            this.setContainRecommend(reader.getAttrAsBoolean("containRecommend", true));
            this.setContainAction(reader.getAttrAsBoolean("containAction", true));
            this.setContainTemplate(reader.getAttrAsBoolean("containTemplate", true));
            this.setContainFileContent(reader.getAttrAsBoolean("containFileContent", false));
            this.setNeedSegmentationCheckbox(reader.getAttrAsBoolean("needSegmentationCheckbox", true));
            this.setNeedIntelligentCustomerService(reader.getAttrAsBoolean("needIntelligentCustomerService", true));
            this.setShortcuts(reader.getAttrAsString("shortcuts", getDefaultShortCuts()));
            this.setNeedRemind(reader.getAttrAsBoolean("needRemind", true));
            this.setOperateCount(reader.getAttrAsInt("operateCount", 0));

        }

    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("AlphaFineConfigManager");
        writer.attr("enabled", this.isEnabled())
                .attr("searchOnline", this.isSearchOnLine())
                .attr("shortcuts", this.getShortcuts())
                .attr("containRecommend", this.isContainRecommend())
                .attr("containAction", this.isContainAction())
                .attr("containDocument", this.isContainDocument())
                .attr("containTemplate", this.isContainTemplate())
                .attr("containPlugin", this.isContainPlugin())
                .attr("containFileContent", this.isContainFileContent())
                .attr("needRemind", this.isNeedRemind())
                .attr("operateCount", this.getOperateCount())
                .attr("needSegmentationCheckbox", this.isNeedSegmentationCheckbox())
                .attr("needIntelligentCustomerService", this.isNeedIntelligentCustomerService());
        writer.end();
    }

    public boolean isSearchOnLine() {
        return searchOnLine;
    }

    public void setSearchOnLine(boolean searchOnLine) {
        this.searchOnLine = searchOnLine;
    }

    public String getShortcuts() {
        if (StringUtils.isBlank(shortcuts)) {
            return getDefaultShortCuts();
        }
        return shortcuts;
    }

    public void setShortcuts(String shortcuts) {
        this.shortcuts = shortcuts;
        this.shortCutKeyStore = convert2KeyStroke(this.shortcuts);
    }

    /**
     * 返回默认快捷键
     *
     * @return
     */
    private String getDefaultShortCuts() {
        return OperatingSystem.isMacOS() ? "meta + D" : "ctrl + D";
    }

    public boolean isContainAction() {
        return containAction;
    }

    public void setContainAction(boolean containAction) {
        this.containAction = containAction;
    }

    public boolean isContainDocument() {
        return containDocument;
    }

    public void setContainDocument(boolean containDocument) {
        this.containDocument = containDocument;
    }

    public boolean isContainTemplate() {
        return containTemplate;
    }

    public void setContainTemplate(boolean containTemplate) {
        this.containTemplate = containTemplate;
    }

    public boolean isContainPlugin() {
        return containPlugin;
    }

    public void setContainPlugin(boolean containPlugin) {
        this.containPlugin = containPlugin;
    }

    public boolean isContainRecommend() {
        return containRecommend;
    }

    public void setContainRecommend(boolean containConclude) {
        this.containRecommend = containConclude;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public KeyStroke getShortCutKeyStore() {
        if (shortCutKeyStore == null) {
            shortCutKeyStore = convert2KeyStroke(this.getShortcuts());
        }
        return shortCutKeyStore;
    }

    public void setShortCutKeyStore(KeyStroke shortCutKeyStore) {
        this.shortCutKeyStore = shortCutKeyStore;
    }

    private KeyStroke convert2KeyStroke(String ks) {
        return KeyStroke.getKeyStroke(ks.replace("+", "pressed"));
    }

    public boolean isContainFileContent() {
        return containFileContent;
    }

    public void setContainFileContent(boolean containFileContent) {
        this.containFileContent = containFileContent;
    }

    public boolean isNeedRemind() {
        return needRemind;
    }

    public void setNeedRemind(boolean needRemind) {
        this.needRemind = needRemind;
    }

    public boolean isNeedSegmentationCheckbox() {
        return needSegmentationCheckbox;
    }

    public void setNeedSegmentationCheckbox(boolean needSegmentationCheckbox) {
        this.needSegmentationCheckbox = needSegmentationCheckbox;
    }

    public boolean isNeedIntelligentCustomerService() {
        return needIntelligentCustomerService;
    }

    public void setNeedIntelligentCustomerService(boolean needIntelligentCustomerService) {
        this.needIntelligentCustomerService = needIntelligentCustomerService;
    }

    public int getOperateCount() {
        return operateCount;
    }

    public void setOperateCount(int operateCount) {
        this.operateCount = operateCount;
    }
}
