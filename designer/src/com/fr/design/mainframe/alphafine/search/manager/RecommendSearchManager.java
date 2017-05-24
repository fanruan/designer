package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
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

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class RecommendSearchManager implements AlphaFineSearchProcessor {
    private static RecommendSearchManager recommendSearchManager = null;
    private SearchResult modelList;
    //todo:for test
    private static final String SEARCHAPI = "http://localhost:8080/monitor/alphafine/search/recommend?searchKey=";

    public synchronized static RecommendSearchManager getRecommendSearchManager() {
        if (recommendSearchManager == null) {
            recommendSearchManager = new RecommendSearchManager();
        }
        return recommendSearchManager;
    }
    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
        if (DesignerEnvManager.getEnvManager().getAlphafineConfigManager().isContainRecommend()) {
            String result;
            this.modelList = new SearchResult();
            HttpClient httpClient = new HttpClient(SEARCHAPI + CodeUtils.cjkEncode(searchText));
            httpClient.asGet();
            httpClient.setTimeout(5000);
            if (!httpClient.isServerAlive()) {
                return modelList;
            }
            result = httpClient.getResponseText();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equals("success")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("result");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AlphaCellModel alphaCellModel = CellModelHelper.getModelFromJson((JSONObject) jsonArray.get(i));
                            if (!RecentSearchManager.getRecentSearchManger().getRecentModelList().contains(alphaCellModel)) {
                                this.modelList.add(alphaCellModel);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                FRLogger.getLogger().error("data transform error! :" + e.getMessage());
            }
            if (modelList.size() > 0) {
                modelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Conclude"), false));
            }
        }

        return modelList;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return new SearchResult();
    }

}
