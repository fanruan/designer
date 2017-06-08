package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.DocumentModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class DocumentSearchManager implements AlphaFineSearchProcessor {
    private static DocumentSearchManager documentSearchManager = null;
    private SearchResult lessModelList;
    private SearchResult moreModelList;
    private static final MoreModel titleModel = new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP"), CellType.DOCUMENT);

    public synchronized static DocumentSearchManager getDocumentSearchManager() {
        if (documentSearchManager == null) {
            documentSearchManager = new DocumentSearchManager();

        }
        return documentSearchManager;
    }

    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
        lessModelList = new SearchResult();
        moreModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainDocument()) {
            String result;
            String url = AlphaFineConstants.DOCUMENT_SEARCH_URL + searchText + "-1";
            HttpClient httpClient = new HttpClient(url);
            httpClient.setTimeout(5000);
            httpClient.asGet();
            if (!httpClient.isServerAlive()) {
                return getNoConnectList();
            }
            result = httpClient.getResponseText();
            AlphaFineHelper.checkCancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("docdata");
                if (jsonArray != null) {
                    final int length = Math.min(AlphaFineConstants.SHOW_SIZE, jsonArray.length());
                    for (int i = 0; i < length; i++) {
                        AlphaFineHelper.checkCancel();
                        DocumentModel cellModel = getModelFromCloud(jsonArray.optJSONObject(i));
                        this.lessModelList.add(cellModel);
                    }
                    for (int i = length; i < jsonArray.length(); i++) {
                        AlphaFineHelper.checkCancel();
                        DocumentModel cellModel = getModelFromCloud(jsonArray.optJSONObject(i));
                        this.moreModelList.add(cellModel);
                    }
                    if (jsonArray.length() > AlphaFineConstants.SHOW_SIZE) {
                        lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.DOCUMENT));
                    } else  {
                        lessModelList.add(0, titleModel);
                        if (lessModelList.size() == 1) {
                            lessModelList.add(AlphaFineHelper.noResultModel);
                        }
                    }


                }

            } catch (JSONException e) {
                FRLogger.getLogger().error(e.getMessage());
                return getNoResultList();
            }

        }
        return lessModelList;
    }

    private SearchResult getNoResultList() {
        SearchResult result = new SearchResult();
        result.add(0, titleModel);
        result.add(AlphaFineHelper.noResultModel);
        return result;

    }

    private SearchResult getNoConnectList() {
        SearchResult result = new SearchResult();
        result.add(0, titleModel);
        result.add(AlphaFineHelper.noConnectionModel);
        return result;
    }

    /**
     * 根据json信息获取文档model
     * @param object
     * @return
     */
    public static DocumentModel getModelFromCloud(JSONObject object) {
        String name = object.optString("title");
        String content = object.optString("summary");
        int documentId = object.optInt("did");
        return new DocumentModel(name, content, documentId);
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return moreModelList;
    }

}
