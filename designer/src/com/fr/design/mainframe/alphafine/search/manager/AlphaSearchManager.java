package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.Inter;

/**
 * Created by XiaXiang on 2017/3/28.
 */
public class AlphaSearchManager implements AlphaFineSearchProcessor {
    private static AlphaSearchManager searchManager;
    private static PluginSearchManager pluginSearchManager;
    private static DocumentSearchManager documentSearchManager;
    private static FileSearchManager fileSearchManager;
    private static ActionSearchManager actionSearchManager;
    private static RecommendSearchManager recommendSearchManager;
    private static RecentSearchManager recentSearchManager;

    public synchronized static AlphaSearchManager getSearchManager() {
        init();
        return searchManager;

    }

    private synchronized static void init() {
        if (searchManager == null) {
            searchManager = new AlphaSearchManager();
            pluginSearchManager = PluginSearchManager.getPluginSearchManager();
            documentSearchManager = DocumentSearchManager.getDocumentSearchManager();
            fileSearchManager = FileSearchManager.getFileSearchManager();
            actionSearchManager = ActionSearchManager.getActionSearchManager();
            recommendSearchManager = RecommendSearchManager.getRecommendSearchManager();
            recentSearchManager = RecentSearchManager.getRecentSearchManger();
        }
    }

    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
        SearchResult recentModelList = recentSearchManager.getLessSearchResult(searchText);
        SearchResult recommendModelList = recommendSearchManager.getLessSearchResult(searchText);
        SearchResult actionModelList = actionSearchManager.getLessSearchResult(searchText);
        SearchResult fileModelList = fileSearchManager.getLessSearchResult(searchText);
        SearchResult documentModelList = documentSearchManager.getLessSearchResult(searchText);
        SearchResult pluginModelList = pluginSearchManager.getLessSearchResult(searchText);
        recentModelList.addAll(recommendModelList);
        recentModelList.addAll(actionModelList);
        recentModelList.addAll(fileModelList);
        recentModelList.addAll(documentModelList);
        recentModelList.addAll(pluginModelList);
        return recentModelList;
    }

    public SearchResult showDefaultSearchResult() {
        SearchResult searchResult = new SearchResult();
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Latest")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Conclude")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_Set")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_Templates")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon")));
        return searchResult;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return null;
    }

}
