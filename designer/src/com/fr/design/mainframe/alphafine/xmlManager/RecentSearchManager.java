package com.fr.design.mainframe.alphafine.xmlManager;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.file.XMLFileManager;
import com.fr.general.IOUtils;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XiaXiang on 2017/5/15.
 */
public class RecentSearchManager extends XMLFileManager {

    private static final String XML_TAG = "AlphafineRecent";

    private List<String> fileList;

    private List<String> actionList;

    private List<String> documentList;

    private List<String> pluginList;

    private Map<String, Integer> modelMap = new HashMap<>();

    private static RecentSearchManager recentSearchManager = null;

    public synchronized static RecentSearchManager getInstance() {
        if (recentSearchManager == null) {
            recentSearchManager = new RecentSearchManager();
        }
        try {
            XMLTools.readFileXML(recentSearchManager, recentSearchManager.getRecentEnvFile());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return recentSearchManager;

    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            reader.readXMLObject(new XMLReadable() {
                public void readXML(XMLableReader reader) {
                    if (reader.isChildNode()) {
                        String nodeName = reader.getTagName();
                        if (nodeName.equals("RecentModel")) {
                            modelMap.put(reader.getAttrAsString("name", StringUtils.EMPTY), reader.getAttrAsInt("celltype", 0));
                        }
                    }
                }
            });
        }

    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        if (!modelMap.isEmpty()) {
            for (String name : modelMap.keySet()) {
                writer.startTAG("RecentModel");
                writer.attr("name", name);
                writer.attr("celltype", modelMap.get(name));
                writer.end();
            }
        }
        writer.end();

    }

    @Override
    public String fileName() {
        return "alphafine_recent.xml";
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    public List<String> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<String> documentList) {
        this.documentList = documentList;
    }

    public List<String> getPluginList() {
        return pluginList;
    }

    public void setPluginList(List<String> pluginList) {
        this.pluginList = pluginList;
    }

    public Map<String, Integer> getModelMap() {
        return modelMap;
    }

    public void setModelMap(Map<String, Integer> modelMap) {
        this.modelMap = modelMap;
    }

    private static File recentFile = null;

    private File getRecentFile() {
        if (recentFile == null) {
            recentFile = new File(ProductConstants.getEnvHome() + File.separator + fileName());
        }
        return recentFile;
    }

    private File getRecentEnvFile() {
        File envFile = getRecentFile();
        if (!envFile.exists()) {
            createRecentFile(envFile);
        }

        return envFile;
    }

    private void createRecentFile(File envFile) {
        try {
            FileWriter fileWriter = new FileWriter(envFile);
            StringReader stringReader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><AlphafineRecent></AlphafineRecent>");
            Utils.copyCharTo(stringReader, fileWriter);
            stringReader.close();
            fileWriter.close();
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 保存设计器的配置文件, 该文件不在env的resource目录下
     * 而是在Consts.getEnvHome() + File.separator + Consts.APP_NAME
     *
     *
     * @date 2014-9-29-上午11:04:23
     *
     */
    public void saveXMLFile() {
        File xmlFile = this.getRecentEnvFile();
        if (xmlFile == null) {
            return;
        }
        if (!xmlFile.getParentFile().exists()) {//建立目录.
            StableUtils.mkdirs(xmlFile.getParentFile());
        }

        String tempName = xmlFile.getName() + ProjectConstants.TEMP_SUFFIX;
        File tempFile = new File(xmlFile.getParentFile(), tempName);

        writeTempFile(tempFile);
        IOUtils.renameTo(tempFile, xmlFile);
    }

    private void writeTempFile(File tempFile){
        try{
            OutputStream fout = new FileOutputStream(tempFile);
            XMLTools.writeOutputStreamXML(this, fout);
            fout.flush();
            fout.close();
        }catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }
}
