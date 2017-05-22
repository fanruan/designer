package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.cellModel.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.file.XMLFileManager;
import com.fr.general.FRLogger;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XiaXiang on 2017/5/15.
 */
public class RecentSearchManager extends XMLFileManager implements AlphaFineSearchProcessor {

    private static final String XML_TAG = "AlphafineRecent";
    private static RecentSearchManager recentSearchManager = null;
    private static File recentFile = null;
    private List<String> fileList;
    private List<String> actionList;
    private List<String> documentList;
    private SearchResult modelList;
    private List<String> pluginList;
    private List<AlphaCellModel> recentModelList = new ArrayList<>();
    private Map<String, List<AlphaCellModel>> recentKVModelMap = new HashMap<>();

    public synchronized static RecentSearchManager getInstance() {
        if (recentSearchManager == null) {
            recentSearchManager = new RecentSearchManager();
            try {
                XMLTools.readFileXML(recentSearchManager, recentSearchManager.getRecentEnvFile());
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
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
                        if (nodeName.equals("RecentModelList")) {
                            String key = reader.getAttrAsString("searchKey", StringUtils.EMPTY);
                            final ArrayList<AlphaCellModel> list = new ArrayList<AlphaCellModel>();
                            reader.readXMLObject(new XMLReadable() {
                                                     @Override
                                                     public void readXML(XMLableReader reader) {
                                                         if (reader.isChildNode()) {
                                                             String nodeName = reader.getTagName();
                                                             if (nodeName.equals("model")) {
                                                                 String name = reader.getAttrAsString("cellModel", StringUtils.EMPTY);
                                                                 try {
                                                                     list.add(CellModelHelper.jsonToModel(new JSONObject(name)));
                                                                 } catch (JSONException e) {
                                                                     FRLogger.getLogger().error(e.getMessage());
                                                                 }
                                                             }
                                                         }
                                                     }
                                                 }
                            );
                            recentKVModelMap.put(key, list);
                        }
                    }
                }
            });
        }

    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        if (!recentKVModelMap.isEmpty()) {
            for (String key : recentKVModelMap.keySet()) {
                writer.startTAG("RecentModelList");
                writer.attr("searchKey", key);

                for (AlphaCellModel model : recentKVModelMap.get(key)) {
                    try {
                        String name = model.ModelToJson().toString();
                        writer.startTAG("model");
                        writer.attr("cellModel", name);
                        writer.end();
                    } catch (JSONException e) {
                        FRLogger.getLogger().error(e.getMessage());
                    }
                }

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
     * 保存XML到设计器安装目录
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

    private void writeTempFile(File tempFile) {
        try {
            OutputStream fout = new FileOutputStream(tempFile);
            XMLTools.writeOutputStreamXML(this, fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }


    public List<AlphaCellModel> getRecentModelList() {
        return recentModelList;
    }

    public void setRecentModelList(List<AlphaCellModel> recentModelList) {
        this.recentModelList = recentModelList;
    }

    public List<AlphaCellModel> getRecentModelList(String searchText) {
        for (String key : recentKVModelMap.keySet()) {
            if (key.equals(searchText)) {
                List<AlphaCellModel> list = recentKVModelMap.get(searchText);
                return list;
            }
        }
        return recentModelList;
    }

    public void addRecentModel(String searchKey, AlphaCellModel cellModel) {

        //final AlphaCellModel alphaCellModel = getCellModel(cellModel);
        if (recentKVModelMap.keySet().contains(searchKey)) {
            List<AlphaCellModel> cellModels = recentKVModelMap.get(searchKey);
            if (cellModels.contains(cellModel)) {
                cellModels.remove(cellModel);
                cellModels.add(cellModel);
            } else {
                cellModels.add(cellModel);
            }
        } else {
            List<AlphaCellModel> list = new ArrayList<>();
            list.add(cellModel);
            recentKVModelMap.put(searchKey, list);
        }
//        if (alphaCellModel != null) {
//            moveOnTop(alphaCellModel);
//        } else {
//            this.recentModelList.add(cellModel);
//        }
//        trimToSize();
    }

    private void moveOnTop(AlphaCellModel alphaCellModel) {
        recentModelList.remove(alphaCellModel);
        recentModelList.add(alphaCellModel);
    }

    private synchronized void trimToSize() {
        if (recentModelList.size() > AlphaFineConstants.MAX_FILE_SIZE) {
            recentModelList.remove(0);
        }
    }

    private synchronized AlphaCellModel getCellModel(AlphaCellModel model) {
        for (int i = recentModelList.size() - 1; i >= 0; i--) {
            final AlphaCellModel entry = recentModelList.get(i);
            String name = entry.getName();
            if (name.equals(model.getName())) {
                return entry;
            }
        }
        return null;
    }

    public Map<String, List<AlphaCellModel>> getRecentKVModelMap() {
        return recentKVModelMap;
    }

    public void setRecentKVModelMap(Map<String, List<AlphaCellModel>> recentKVModelMap) {
        this.recentKVModelMap = recentKVModelMap;
    }

    @Override
    public SearchResult showLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        recentModelList = getRecentModelList(searchText);
        if (recentModelList != null && recentModelList.size() > 0) {
            modelList.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Latest"), false));
        }
        modelList.addAll(recentModelList);
        return modelList;
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return new SearchResult();
    }

//    public List<recentKVModel> getRecentKVModelList() {
//        return recentKVModelList;
//    }
//
//    public void setRecentKVModelList(List<recentKVModel> recentKVModelList) {
//        this.recentKVModelList = recentKVModelList;
//    }
}
