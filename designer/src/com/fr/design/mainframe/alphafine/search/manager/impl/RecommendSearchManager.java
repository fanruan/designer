package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.CodeUtils;
import com.fr.stable.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class RecommendSearchManager implements AlphaFineSearchProvider {
    private static RecommendSearchManager recommendSearchManager = null;
    private SearchResult modelList;
    private SearchResult recommendModelList;

    public synchronized static RecommendSearchManager getInstance() {
        if (recommendSearchManager == null) {
            recommendSearchManager = new RecommendSearchManager();
        }
        return recommendSearchManager;
    }

    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
        searchText = searchText.replaceAll(StringUtils.BLANK, StringUtils.EMPTY);
        this.modelList = new SearchResult();
        this.recommendModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainRecommend()) {
            String result;
            HttpClient httpClient = new HttpClient(AlphaFineConstants.SEARCH_API + CodeUtils.cjkEncode(searchText));
            httpClient.asGet();
            if (!httpClient.isServerAlive()) {
                return getNoConnectList();
            }
            httpClient.setTimeout(3000);
            result = httpClient.getResponseText();
            AlphaFineHelper.checkCancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equals("success")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("result");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AlphaFineHelper.checkCancel();
                            AlphaCellModel alphaCellModel = CellModelHelper.getModelFromJson((JSONObject) jsonArray.get(i));
                            if (alphaCellModel != null && !alreadyContain(alphaCellModel)) {
                                this.recommendModelList.add(alphaCellModel);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                FRLogger.getLogger().error("recommend search error! :" + e.getMessage());
            }
            Iterator<AlphaCellModel> modelIterator = recommendModelList.iterator();
            while (modelIterator.hasNext()) {
                AlphaCellModel model = modelIterator.next();
                if (model.getType() == CellType.ACTION && !((ActionModel) model).getAction().isEnabled()) {
                    modelIterator.remove();
                }
            }
            if (recommendModelList.size() > 0) {
                modelList.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Recommend"), false));
                modelList.addAll(recommendModelList);
            }
        }
        return modelList;
    }

    /**
     * 是否已包含该model
     *
     * @param cellModel
     * @return
     */
    private boolean alreadyContain(AlphaCellModel cellModel) {
        return RecentSearchManager.getInstance().getRecentModelList().contains(cellModel) || this.recommendModelList.contains(cellModel);
    }

    private SearchResult getNoConnectList() {
        SearchResult result = new SearchResult();
        result.add(0, new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Recommend")));
        result.add(AlphaFineHelper.NO_CONNECTION_MODEL);
        return result;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return new SearchResult();
    }

    public List<AlphaCellModel> getRecommendModelList() {
        return recommendModelList;
    }

}
