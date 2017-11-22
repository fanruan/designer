package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.FileModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class FileSearchManager implements AlphaFineSearchProcessor {
    private static final int MARK_LENGTH = 6;
    private static final String DS_NAME = "dsname=\"";
    private static final String FRM_PREFIX = "k:frm ";
    private static final String CPT_PREFIX = "k:cpt ";
    private static FileSearchManager fileSearchManager = null;
    private SearchResult filterModelList;
    private SearchResult lessModelList;
    private SearchResult moreModelList;
    private List<FileNode> fileNodes = null;
    //隐藏的搜索功能，可根据特殊的字符标记判断搜索分类
    private boolean isContainCpt = true;
    private boolean isContainFrm = true;

    public synchronized static FileSearchManager getFileSearchManager() {
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
        if (searchText.startsWith(FRM_PREFIX)) {
            isContainCpt = false;
            searchText = searchText.substring(MARK_LENGTH, searchText.length());
        } else if (searchText.startsWith(CPT_PREFIX)) {
            isContainFrm = false;
            searchText = searchText.substring(MARK_LENGTH, searchText.length());
        }
        if (StringUtils.isBlank(searchText) || ComparatorUtils.equals(searchText, DS_NAME)) {
            lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer_Templates")));
            return lessModelList;
        }

        Env env = FRContext.getCurrentEnv();
        fileNodes = new ArrayList<>();
        fileNodes = listTpl(env, ProjectConstants.REPORTLETS_NAME, true);
        AlphaFineHelper.checkCancel();
        isContainCpt = true;
        isContainFrm = true;
        for (FileNode node : fileNodes) {
            boolean isAlreadyContain = false;
            String fileEnvPath = node.getEnvPath();
            String filePath = StableUtils.pathJoin(env.getPath(), fileEnvPath);
            isAlreadyContain = searchFile(searchText, node, isAlreadyContain);
            searchFileContent(searchText, node, isAlreadyContain, filePath);

        }
        SearchResult result = new SearchResult();
        for (AlphaCellModel object : filterModelList) {
            if (!AlphaFineHelper.getFilterResult().contains(object)) {
                result.add(object);
            }

        }
        if (result.isEmpty()) {
            return lessModelList;
        } else if (result.size() < AlphaFineConstants.SHOW_SIZE + 1) {
            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Templates")));
            lessModelList.addAll(result);
        } else {
            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Templates"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"), true, CellType.FILE));
            lessModelList.addAll(result.subList(0, AlphaFineConstants.SHOW_SIZE));
            moreModelList.addAll(result.subList(AlphaFineConstants.SHOW_SIZE, result.size()));
        }

        return this.lessModelList;
    }

    /**
     * 搜索文件内容
     *
     * @param searchText
     * @param node
     * @param isAlreadyContain
     * @param filePath
     */
    private void searchFileContent(String searchText, FileNode node, boolean isAlreadyContain, String filePath) {
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainFileContent()) {

            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
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
                    this.filterModelList.add(model);
                }
                isr.close();
                reader.close();
            } catch (FileNotFoundException e) {
                FRLogger.getLogger().error(e.getMessage());
            } catch (IOException e) {
                FRLogger.getLogger().error(e.getMessage());
            }
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
    private boolean searchFile(String searchText, FileNode node, boolean isAlreadyContain) {
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainTemplate()) {
            if (node.getName().toLowerCase().contains(searchText)) {
                FileModel model = new FileModel(node.getName(), node.getEnvPath());
                this.filterModelList.add(model);
                isAlreadyContain = true;
            }
        }
        return isAlreadyContain;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return moreModelList;
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
}