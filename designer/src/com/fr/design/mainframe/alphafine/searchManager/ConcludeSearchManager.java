package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class ConcludeSearchManager implements AlphaFineSearchProcessor {
    private static ConcludeSearchManager concludeSearchManager = null;
    private SearchResult modelList;

    public synchronized static ConcludeSearchManager getConcludeSearchManager() {
        if (concludeSearchManager == null) {
            concludeSearchManager = new ConcludeSearchManager();
        }
        return concludeSearchManager;
    }
    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        modelList.add(new MoreModel("猜您需要", false));
        return modelList;
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return null;
    }

}
