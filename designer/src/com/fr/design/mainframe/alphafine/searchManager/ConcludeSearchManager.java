package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.Inter;

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
        //todo: 猜您喜欢逻辑需要重新设计
        this.modelList = new SearchResult();
        modelList.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Conclude"), false));
        return modelList;
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return null;
    }

}
