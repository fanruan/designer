package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.mainframe.alphafine.model.SearchResult;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public interface AlphaFineSearchProcessor {
    SearchResult showLessSearchResult(String searchText);

    SearchResult showMoreSearchResult();
}
