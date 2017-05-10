package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class LatestSearchManager implements AlphaFineSearchProcessor {
    private static LatestSearchManager latestSearchManager = null;
    private SearchResult modelList;
    private SearchResult latestModelList;

    public synchronized static LatestSearchManager getLatestSearchManager() {
        if (latestSearchManager == null) {
            latestSearchManager = new LatestSearchManager();
        }
        return latestSearchManager;
    }
    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        modelList.add(new MoreModel("本地常用", false));
        //todo: 常用逻辑需要重新设计
//        if (getLatestModelList() != null && getLatestModelList().size() > 0) {
//            modelList.addAll(getLatestModelList());
//        }
        return modelList;
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return null;
    }

    public SearchResult getModelList() {
        return modelList;
    }

    public void setModelList(SearchResult modelList) {
        this.modelList = modelList;
    }

    public SearchResult getLatestModelList() {
        if(this.latestModelList != null && this.latestModelList.size() > 0) {
            return this.latestModelList;
        }
        return AlphaSearchManager.getSearchManager().getLatestSearchResult();
    }

    public void setLatestModelList(SearchResult latestModelList) {
        this.latestModelList = latestModelList;
    }
}
