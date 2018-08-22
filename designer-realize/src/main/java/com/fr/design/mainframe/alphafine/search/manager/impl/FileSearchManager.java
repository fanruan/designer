package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.base.FRContext;
import com.fr.base.extension.FileExtension;
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
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;


/**
 * Created by XiaXiang on 2017/3/27.
 */
public class FileSearchManager implements AlphaFineSearchProvider {
    private static final int MARK_LENGTH = 6;
    private static final String DS_NAME = "dsname=\"";
    private static final String FRM_PREFIX = "k:frm ";
    private static final String CPT_PREFIX = "k:cpt ";
    private static volatile FileSearchManager instance;
    private SearchResult filterModelList;
    private SearchResult lessModelList;
    private SearchResult moreModelList;
    private String searchText;
    private FileNode[] fileNodes = null;

    //停止搜索
//隐藏的搜索功能，可根据特殊的字符标记判断搜索分类
    private boolean isContainCpt = true;
    private boolean isContainFrm = true;

    public static FileSearchManager getInstance() {
        if (instance == null) {
            synchronized (FileSearchManager.class) {
                if (instance == null) {
                    instance = new FileSearchManager();
                }
            }
        }
        return instance;
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

    public SearchResult getLessSearchResult(String[] searchText) {
        this.filterModelList = new SearchResult();
        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        for (int j = 0; j < searchText.length; j++) {
            this.searchText = dealWithSearchText(searchText[j]);
            if (StringUtils.isBlank(this.searchText) || ComparatorUtils.equals(this.searchText, DS_NAME)) {
                lessModelList.add(new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Templates")));
                return lessModelList;
            }
            AlphaFineHelper.checkCancel();
            fileNodes = FRContext.getFileNodes().list(ProjectConstants.REPORTLETS_NAME, new FileExtension[]{FileExtension.CPT, FileExtension.FRM}, true);
            isContainCpt = true;
            isContainFrm = true;
            doSearch(this.searchText);
        }

        if (filterModelList.isEmpty()) {
            return new SearchResult();
        } else if (filterModelList.size() < AlphaFineConstants.SHOW_SIZE + 1) {
            lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Templates")));
            lessModelList.addAll(filterModelList);
        } else {
            lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Templates"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.FILE));
            lessModelList.addAll(filterModelList.subList(0, AlphaFineConstants.SHOW_SIZE));
            moreModelList.addAll(filterModelList.subList(AlphaFineConstants.SHOW_SIZE, filterModelList.size()));

        }
        return lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return moreModelList;
    }

    private void doSearch(String searchText) {
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainTemplate()) {
            for (FileNode node : fileNodes) {
                if (node.getName().toLowerCase().contains(searchText)) {
                    FileModel model = new FileModel(node.getName(), node.getEnvPath());
                    if (!AlphaFineHelper.getFilterResult().contains(model)) {
                        AlphaFineHelper.checkCancel();
                        filterModelList.add(model);
                    }
                }

            }
        }
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainFileContent()) {
            FileNode[] fileNodes = FRContext.getFileNodes().filterFiles(searchText, ProjectConstants.REPORTLETS_NAME, new FileExtension[]{FileExtension.CPT, FileExtension.FRM}, true);
            for (FileNode node : fileNodes) {
                FileModel model = new FileModel(node.getName(), node.getEnvPath());
                if (!AlphaFineHelper.getFilterResult().contains(model) && !filterModelList.contains(model)) {
                    AlphaFineHelper.checkCancel();
                    filterModelList.add(model);
                }
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
