package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.toolbar.UpdateActionManager;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by XiaXiang on 2017/5/15.
 */
public class RecentSearchManager extends XMLFileManager implements AlphaFineSearchProcessor {

    private static final String XML_TAG = "AlphaFineRecent";
    private static final int MAX_SIZE = 3;
    private static RecentSearchManager recentSearchManager = null;
    private static File recentFile = null;
    private SearchResult modelList;
    private SearchResult recentModelList;
    private Map<String, SearchResult> recentKVModelMap = new HashMap<>();

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
                            final SearchResult list = new SearchResult();
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
                String modelValue = reader.getAttrAsString("cellModel", StringUtils.EMPTY);
                addModelToList(list, modelValue);
            }
        }
    }


    private void addModelToList(List<AlphaCellModel> list, String modelValue) {
        try {
            AlphaCellModel model = CellModelHelper.getModelFromJson(new JSONObject(modelValue));
            if (model != null) {
                list.add(model);
            }
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
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
                        String modelValue = model.ModelToJson().toString();
                        writer.startTAG("model");
                        writer.attr("cellModel", modelValue);
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
        return "AlphaFine_Recent.xml";
    }


    /**
     * 获取xml
     *
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
     *
     * @param envFile
     */
    private void createRecentFile(File envFile) {
        try {
            FileWriter fileWriter = new FileWriter(envFile);
            StringReader stringReader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><AlphaFineRecent></AlphaFineRecent>");
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

    /**
     * 根据搜索字段获取对应的model列表
     *
     * @param searchText
     * @return
     */
    private synchronized SearchResult getRecentModelList(String searchText) {
        recentModelList = new SearchResult();
        for (String key : recentKVModelMap.keySet()) {
            AlphaFineHelper.checkCancel();
            if (ComparatorUtils.equals(key, searchText)) {
                recentModelList = recentKVModelMap.get(searchText);
                SearchResult resultModelList = recentModelList;
                Iterator<AlphaCellModel> modelIterator = resultModelList.iterator();
                SearchResult searchResult = new SearchResult();
                while (modelIterator.hasNext()) {
                    AlphaCellModel model = modelIterator.next();
                    if (model.getType() == CellType.ACTION) {
                        UpdateAction action = UpdateActionManager.getUpdateActionManager().getActionByName(model.getName());
                        if (action != null) {
                            ((ActionModel) model).setAction(action);
                            searchResult.add(model);
                        }
                    } else {
                        searchResult.add(model);
                    }

                }
                Collections.sort(searchResult);
                int size = searchResult.size();
                if (size > MAX_SIZE) {
                    SearchResult result = new SearchResult();
                    result.addAll(searchResult.subList(0, MAX_SIZE));
                    return result;
                }
                return searchResult;
            }
        }
        return recentModelList;
    }

    /**
     * 将搜索结果加入到当前MAP中
     *
     * @param searchKey
     * @param cellModel
     */
    public void addRecentModel(String searchKey, AlphaCellModel cellModel) {
        if (recentKVModelMap.keySet().contains(searchKey)) {
            List<AlphaCellModel> cellModels = recentKVModelMap.get(searchKey);
            int index = cellModels.indexOf(cellModel);
            if (index >= 0) {
                cellModels.get(index).addSearchCount();
            } else {
                cellModels.add(cellModel);
            }
            //trimToSize(cellModels);
        } else {
            SearchResult list = new SearchResult();
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
    public synchronized SearchResult getLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        recentModelList = getRecentModelList(searchText);
        if (recentModelList != null && recentModelList.size() > 0) {
            modelList.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Latest")));
        }
        modelList.addAll(recentModelList);
        return modelList;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return new SearchResult();
    }

}
