package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.CodeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class RecommendSearchManager implements AlphaFineSearchProcessor {
    //todo:for test
    private static final String SEARCHAPI = "http://localhost:8080/monitor/alphafine/search/recommend?searchKey=";
    private static RecommendSearchManager recommendSearchManager = null;
    private SearchResult modelList;
    private List<AlphaCellModel> recommendModelList = new ArrayList<>();

    public synchronized static RecommendSearchManager getRecommendSearchManager() {
        if (recommendSearchManager == null) {
            recommendSearchManager = new RecommendSearchManager();
        }
        return recommendSearchManager;
    }

    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
        this.modelList = new SearchResult();
        this.recommendModelList = new ArrayList<>();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainRecommend()) {
            String result;
            HttpClient httpClient = new HttpClient(SEARCHAPI + CodeUtils.cjkEncode(searchText));
            httpClient.asGet();
            httpClient.setTimeout(5000);
            if (!httpClient.isServerAlive()) {
                return getNoConnectList();
            }
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
                            if (alphaCellModel != null && !RecentSearchManager.getRecentSearchManger().getRecentModelList().contains(alphaCellModel)) {
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

    private SearchResult getNoConnectList() {
        SearchResult result = new SearchResult();
        result.add(0, new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Recommend"), false));
        result.add(AlphaFineHelper.NO_CONNECTION_MODEL);
        return result;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return new SearchResult();
    }

    public List<AlphaCellModel> getRecommendModelList() {
        return recommendModelList;
    }

    public void setRecommendModelList(List<AlphaCellModel> recommendModelList) {
        this.recommendModelList = recommendModelList;
    }
}
