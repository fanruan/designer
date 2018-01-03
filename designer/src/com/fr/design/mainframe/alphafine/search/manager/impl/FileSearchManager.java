package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.FileModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class FileSearchManager implements AlphaFineSearchProvider {
    private static final int MARK_LENGTH = 6;
    private static final String DS_NAME = "dsname=\"";
    private static final String FRM_PREFIX = "k:frm ";
    private static final String CPT_PREFIX = "k:cpt ";
    private static FileSearchManager fileSearchManager = null;
    private SearchResult filterModelList;
    private SearchResult lessModelList;
    private SearchResult moreModelList;
    private String searchText;
    private List<FileNode> fileNodes = null;
    //停止搜索
    private boolean stopSearch = false;
    //隐藏的搜索功能，可根据特殊的字符标记判断搜索分类
    private boolean isContainCpt = true;
    private boolean isContainFrm = true;

    public synchronized static FileSearchManager getInstance() {
        init();
        return fileSearchManager;
    }

    public synchronized static void init() {
        if (fileSearchManager == null) {
            fileSearchManager = new FileSearchManager();
        }
    }

    /**
     * 根据文件路径获取文件模型
     *
     * @param object
     * @return
     */
    public static FileModel getModelFromCloud(JSONObject object) {
        String filePath = object.optString("filePath");
        int searchCount = object.optInt("searchCount");
        String name = AlphaFineHelper.findFileName(filePath);
        return new FileModel(name, filePath, searchCount);
    }

    public synchronized SearchResult getLessSearchResult(String searchText) {
        this.filterModelList = new SearchResult();
        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        this.searchText = dealWithSearchText(searchText);
        if (StringUtils.isBlank(this.searchText) || ComparatorUtils.equals(this.searchText, DS_NAME)) {
            lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer_Templates")));
            return lessModelList;
        }
        Env env = FRContext.getCurrentEnv();
        fileNodes = new ArrayList<>();
        fileNodes = listTpl(env, ProjectConstants.REPORTLETS_NAME, true);
        AlphaFineHelper.checkCancel();
        isContainCpt = true;
        isContainFrm = true;
        doSearch(this.searchText, true, env);
        if (stopSearch) {
            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Templates"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"), true, CellType.FILE));
            lessModelList.addAll(filterModelList.subList(0, AlphaFineConstants.SHOW_SIZE));
            stopSearch = false;
            return this.lessModelList;
        }
        if (filterModelList.isEmpty()) {
            return new SearchResult();
        }
        lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Templates"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"), false, CellType.FILE));
        lessModelList.addAll(filterModelList);
        return lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        if (moreModelList != null && !moreModelList.isEmpty()) {
            return moreModelList;
        }
        this.filterModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        Env env = FRContext.getCurrentEnv();
        AlphaFineHelper.checkCancel();
        isContainCpt = true;
        isContainFrm = true;
        doSearch(this.searchText, false, env);
        moreModelList.addAll(filterModelList.subList(AlphaFineConstants.SHOW_SIZE, filterModelList.size()));
        return moreModelList;
    }

    private void doSearch(String searchText, boolean needMore, Env env) {
        for (FileNode node : fileNodes) {
            boolean isAlreadyContain = false;
            isAlreadyContain = searchFile(searchText, node, isAlreadyContain, needMore);
            if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainFileContent() && node.getLock() == null) {
                searchFileContent(env, searchText, node, isAlreadyContain, needMore);
            }
            if (filterModelList.size() > AlphaFineConstants.SHOW_SIZE && stopSearch) {
                return;
            }

        }
    }

    private String dealWithSearchText(String searchText) {
        if (searchText.startsWith(FRM_PREFIX)) {
            isContainCpt = false;
            searchText = searchText.substring(MARK_LENGTH, searchText.length());
        } else if (searchText.startsWith(CPT_PREFIX)) {
            isContainFrm = false;
            searchText = searchText.substring(MARK_LENGTH, searchText.length());
        }
        return searchText;
    }

    /**
     * 搜索文件内容
     *
     * @param searchText
     * @param node
     * @param isAlreadyContain
     */
    private void searchFileContent(Env env, String searchText, FileNode node, boolean isAlreadyContain, boolean needMore) {
        try {
            InputStream inputStream = env.readBean(node.getEnvPath().substring(ProjectConstants.REPORTLETS_NAME.length() + 1), ProjectConstants.REPORTLETS_NAME);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line;
            int columnNumber;
            boolean isFoundInContent = false;
            while ((line = reader.readLine()) != null) {
                columnNumber = line.toLowerCase().indexOf(searchText);
                if (columnNumber != -1) {
                    isFoundInContent = true;
                    break;
                }
            }
            if (isFoundInContent && !isAlreadyContain) {
                FileModel model = new FileModel(node.getName(), node.getEnvPath());
                if (!AlphaFineHelper.getFilterResult().contains(model)) {
                    AlphaFineHelper.checkCancel();
                    filterModelList.add(model);
                }
                if (this.filterModelList.size() > AlphaFineConstants.SHOW_SIZE && needMore) {
                    stopSearch = true;
                }
            }
            isr.close();
            reader.close();
        } catch (Exception e) {
            FRLogger.getLogger().error("file read error: " + e.getMessage());
        }
    }

    /**
     * 搜索模板
     *
     * @param searchText
     * @param node
     * @param isAlreadyContain
     * @return
     */
    private boolean searchFile(String searchText, FileNode node, boolean isAlreadyContain, boolean needMore) {
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainTemplate()) {
            if (node.getName().toLowerCase().contains(searchText)) {
                FileModel model = new FileModel(node.getName(), node.getEnvPath());
                if (!AlphaFineHelper.getFilterResult().contains(model)) {
                    AlphaFineHelper.checkCancel();
                    filterModelList.add(model);
                }
                if (filterModelList.size() > AlphaFineConstants.SHOW_SIZE && needMore) {
                    stopSearch = true;
                }
                isAlreadyContain = true;
            }
        }
        return isAlreadyContain;
    }

    /**
     * 获取工作目录下所有符合要求的模板
     *
     * @param env
     * @param rootFilePath
     * @param recurse
     * @return
     */
    private List<FileNode> listTpl(Env env, String rootFilePath, boolean recurse) {
        List<FileNode> fileNodeList = new ArrayList<FileNode>();
        try {
            listAll(env, rootFilePath, fileNodeList, recurse);
        } catch (Exception e) {
            FRContext.getLogger().error("file search error: " + e.getMessage(), e);
        }
        return fileNodeList;
    }

    /**
     * 获取当前工作目录下所有模板
     *
     * @param env
     * @param rootFilePath
     * @param nodeList
     * @param recurse
     * @throws Exception
     */
    private void listAll(Env env, String rootFilePath, List<FileNode> nodeList, boolean recurse) throws Exception {
        FileNode[] fns = env.listFile(rootFilePath);
        for (int i = 0; i < fns.length; i++) {
            FileNode fileNode = fns[i];
            if (fileNode.isDirectory()) {
                if (recurse) {
                    listAll(env, rootFilePath + File.separator + fns[i].getName(), nodeList, true);
                } else {
                    nodeList.add(fns[i]);
                }
            } else if (isContainCpt && fileNode.isFileType("cpt")) {
                nodeList.add(fileNode);
            } else if (isContainFrm && fileNode.isFileType("frm")) {
                nodeList.add(fileNode);
            }
        }
    }

    /**
     * 是否包含cpt
     *
     * @return
     */
    public boolean isContainCpt() {
        return isContainCpt;
    }

    public void setContainCpt(boolean containCpt) {
        isContainCpt = containCpt;
    }

    /**
     * 是否包含frm
     *
     * @return
     */
    public boolean isContainFrm() {
        return isContainFrm;
    }

    public void setContainFrm(boolean containFrm) {
        isContainFrm = containFrm;
    }

    public SearchResult getMoreModelList() {
        return moreModelList;
    }

    public void setMoreModelList(SearchResult moreModelList) {
        this.moreModelList = moreModelList;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}