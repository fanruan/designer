package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.FileModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.file.filetree.FileNode;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class FileSearchManager implements AlphaFineSearchProcessor {
    private SearchResult filterModelList;
    private SearchResult lessModelList;
    private SearchResult moreModelList;
    private List<FileNode> fileNodes = null;
    private static FileSearchManager fileSearchManager = null;

    private static final MoreModel TITLE_MODEL = new MoreModel(Inter.getLocText("FR-Designer_Templates"), CellType.FILE);
    private static final MoreModel MORE_MODEL = new MoreModel(Inter.getLocText("FR-Designer_Templates"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.FILE);

    public synchronized static FileSearchManager getFileSearchManager() {
        init();
        return fileSearchManager;
    }

    public synchronized static void init() {
        if (fileSearchManager == null) {
            fileSearchManager = new FileSearchManager();
        }
    }

    public synchronized SearchResult getLessSearchResult(String searchText) {
        this.filterModelList = new SearchResult();
        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainTemplate()) {
            Env env = FRContext.getCurrentEnv();
            fileNodes = new ArrayList<>();
            fileNodes = listTpl(env, ProjectConstants.REPORTLETS_NAME, true);
            for (FileNode node : fileNodes) {
                boolean isAlreadyContain = false;
                String fileEnvPath = node.getEnvPath();
                String filePath = StableUtils.pathJoin(env.getPath(), fileEnvPath);
                isAlreadyContain = searchFile(searchText, node, isAlreadyContain);
                searchFileContent(searchText, node, isAlreadyContain, filePath);

            }
            SearchResult result = new SearchResult();
            for (Object object : filterModelList) {
                if (!AlphaFineHelper.getFilterResult().contains(object)) {
                    result.add(object);
                }

            }
            if (result.size() < AlphaFineConstants.SHOW_SIZE + 1) {
                lessModelList.add(0, TITLE_MODEL);
                if (result.size() == 0) {
                    lessModelList.add(AlphaFineHelper.NO_RESULT_MODEL);
                } else {
                    lessModelList.addAll(result);
                }
            } else {
                lessModelList.add(0, MORE_MODEL);
                lessModelList.addAll(result.subList(0, AlphaFineConstants.SHOW_SIZE));
                moreModelList.addAll(result.subList(AlphaFineConstants.SHOW_SIZE, result.size()));
            }
        }
        return this.lessModelList;
    }

    /**
     * 搜索文件内容
     * @param searchText
     * @param node
     * @param isAlreadyContain
     * @param filePath
     */
    private void searchFileContent(String searchText, FileNode node, boolean isAlreadyContain, String filePath) {
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainFileContent()) {

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String line;
                int columnNumber;
                boolean isFoundInContent = false;
                while ((line = reader.readLine()) != null) {
                    columnNumber = line.toLowerCase().indexOf(searchText);
                    if (columnNumber != -1) {
                        isFoundInContent = true;
                    }
                }
                if (isFoundInContent && !isAlreadyContain) {
                    FileModel model = new FileModel(node.getName(), node.getEnvPath());
                    this.filterModelList.add(model);
                }
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
     * @param searchText
     * @param node
     * @param isAlreadyContain
     * @return
     */
    private boolean searchFile(String searchText, FileNode node, boolean isAlreadyContain) {
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainTemplate()) {
            if (node.getName().toLowerCase().contains(searchText.toLowerCase())) {
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
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return fileNodeList;
    }

    /**
     * 获取当前工作目录下所有模板
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
            } else if (fileNode.isFileType("cpt") || fileNode.isFileType("frm")) {
                nodeList.add(fileNode);
            }
        }
    }

    /**
     * 根据文件路径获取文件模型
     * @param filePath
     * @return
     */
    public static FileModel getModelFromCloud(String filePath) {
        String name = AlphaFineHelper.findFileName(filePath);
        return new FileModel(name, filePath);
    }

}
