package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.cellModel.DocumentModel;
import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
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
    private SearchResult lessModelList = new SearchResult();
    private SearchResult moreModelList = new SearchResult();

    public synchronized static DocumentSearchManager getDocumentSearchManager() {
        if (documentSearchManager == null) {
            documentSearchManager = new DocumentSearchManager();

        }
        return documentSearchManager;
    }

    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {
        if (DesignerEnvManager.getEnvManager().getAlphafineConfigManager().isContainDocument()) {
            String result;
            String url = AlphaFineConstants.DOCUMENT_SEARCH_URL + searchText + "-1";
            HttpClient httpClient = new HttpClient(url);
            httpClient.setTimeout(5000);
            httpClient.asGet();
            result = httpClient.getResponseText();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("docdata");
                if (jsonArray != null && jsonArray.length() > 0) {
                    final int length = Math.min(AlphaFineConstants.SHOW_SIZE, jsonArray.length());
                    for (int i = 0; i < length; i++) {
                        DocumentModel cellModel = getModelFromCloud(jsonArray.optJSONObject(i));
                        this.lessModelList.add(cellModel);
                    }
                    for (int i = length; i < jsonArray.length(); i++) {
                        DocumentModel cellModel = getModelFromCloud(jsonArray.optJSONObject(i));
                        this.moreModelList.add(cellModel);
                    }
                    if (jsonArray.length() > 0) {
                        if (jsonArray.length() > AlphaFineConstants.SHOW_SIZE) {
                            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.DOCUMENT));
                        } else  {
                            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP"), CellType.DOCUMENT));
                        }
                    }
                }

            } catch (JSONException e) {
                FRLogger.getLogger().error(e.getMessage());
                return lessModelList;
            }


        }
        return lessModelList;
    }

    public static DocumentModel getModelFromCloud(JSONObject object) {
        String name = object.optString("title");
        String content = object.optString("summary");
        int documentId = object.optInt("did");
        return new DocumentModel(name, content, documentId);
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return moreModelList;
    }

}
