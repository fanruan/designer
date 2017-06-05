package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.file.XMLFileManager;
import com.fr.general.ComparatorUtils;
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
    private static final int MAX_SIZE = 3;
    private static RecentSearchManager recentSearchManager = null;
    private static File recentFile = null;
    private SearchResult modelList;
    private List<AlphaCellModel> fileList = new ArrayList<>();
    private List<AlphaCellModel> actionList = new ArrayList<>();
    private List<AlphaCellModel> documentList = new ArrayList<>();
    private List<AlphaCellModel> pluginList = new ArrayList<>();
    private List<AlphaCellModel> recentModelList = new ArrayList<>();
    private Map<String, List<AlphaCellModel>> recentKVModelMap = new HashMap<>();

    public synchronized static RecentSearchManager getRecentSearchManger() {
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
                                                         readCellModel(reader, list);
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

    private void readCellModel(XMLableReader reader, List<AlphaCellModel> list) {
        if (reader.isChildNode()) {
            String nodeName = reader.getTagName();
            if (nodeName.equals("model")) {
                String name = reader.getAttrAsString("cellModel", StringUtils.EMPTY);
                addModelToList(list, name);
            }
        }
    }


    private void addModelToList(List<AlphaCellModel> list, String name) {
        try {
            AlphaCellModel model = getModelFromJson(new JSONObject(name));
            if (model != null) {
                list.add(model);
            }
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    /**
     * 转成cellModel
     * @param object
     * @return
     */
    private AlphaCellModel getModelFromJson(JSONObject object) {
        int typeValue = object.optInt("cellType");
        AlphaCellModel cellModel = null;
        switch (CellType.parse(typeValue)) {
            case ACTION:
                cellModel = ActionSearchManager.getModelFromCloud(object.optString("result"));
                if (cellModel != null) {
                    actionList.add(cellModel);
                }
                break;
            case DOCUMENT:
                cellModel = DocumentSearchManager.getModelFromCloud(object.optJSONObject("result"));
                if (cellModel != null) {
                    documentList.add(cellModel);
                }
                break;
            case FILE:
                cellModel = FileSearchManager.getModelFromCloud(object.optString("result"));
                if (cellModel != null) {
                    fileList.add(cellModel);
                }
                break;
            case PLUGIN:
            case REUSE:
                cellModel = PluginSearchManager.getModelFromCloud(object.optJSONObject("result"));
                if (cellModel != null) {
                    pluginList.add(cellModel);
                }
                break;

        }
        return cellModel;
    }

    public List<AlphaCellModel> getFileList() {
        return fileList;
    }

    public List<AlphaCellModel> getActionList() {
        return actionList;
    }

    public List<AlphaCellModel> getDocumentList() {
        return documentList;
    }

    public List<AlphaCellModel> getPluginList() {
        return pluginList;
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



    /**
     * 获取xml
     * @return
     */
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

    /**
     * 创建XML
     * @param envFile
     */
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

    /**
     * 根据搜索字段获取对应的model列表
     * @param searchText
     * @return
     */
    public List<AlphaCellModel> getRecentModelList(String searchText) {
        recentModelList = new ArrayList<>();
        for (String key : recentKVModelMap.keySet()) {
            if (ComparatorUtils.equals(key, searchText)) {
                recentModelList = recentKVModelMap.get(searchText);
                int size = recentModelList.size();
                if (size > MAX_SIZE) {
                    return recentModelList.subList(size - MAX_SIZE, size);
                }
                return recentModelList;
            }
        }
        return recentModelList;
    }

    /**
     * 将搜索结果加入到当前MAP中
     * @param searchKey
     * @param cellModel
     */
    public void addRecentModel(String searchKey, AlphaCellModel cellModel) {
        if (recentKVModelMap.keySet().contains(searchKey)) {
            List<AlphaCellModel> cellModels = recentKVModelMap.get(searchKey);
            if (cellModels.contains(cellModel)) {
                cellModels.remove(cellModel);
                cellModels.add(cellModel);
            } else {
                cellModels.add(cellModel);
            }
            trimToSize(cellModels);
        } else {
            List<AlphaCellModel> list = new ArrayList<>();
            list.add(cellModel);
            recentKVModelMap.put(searchKey, list);
        }
    }


    private synchronized void trimToSize(List<AlphaCellModel> cellModels) {
        if (cellModels.size() > AlphaFineConstants.MAX_FILE_SIZE) {
            cellModels.remove(0);
        }
    }


    @Override
    public SearchResult getLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        recentModelList = getRecentModelList(searchText);
        if (recentModelList != null && recentModelList.size() > 0) {
            modelList.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Latest"), false));
        }
        modelList.addAll(recentModelList);
        return modelList;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return new SearchResult();
    }

}
