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

import java.security.AlgorithmConstraints;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class DocumentSearchManager implements AlphaFineSearchProcessor {
    private static DocumentSearchManager documentSearchManager = null;
    private SearchResult lessModelList;
    private SearchResult moreModelList;

    public synchronized static DocumentSearchManager getDocumentSearchManager() {
        if (documentSearchManager == null) {
            documentSearchManager = new DocumentSearchManager();

        }
        return documentSearchManager;
    }

    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {
        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphafineConfigManager().isContainDocument()) {
            String result;
            String url = "http://help.finereport.com/?api-search-title-" + searchText + "-1";
            HttpClient httpClient = new HttpClient(url);
            httpClient.setTimeout(5000);
            httpClient.asGet();
            result = httpClient.getResponseText();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = null;
                if (jsonObject.get("docdata") != null) {
                    jsonArray = (JSONArray) jsonObject.get("docdata");
                }
                if (jsonArray.length() > 0) {
                    if (jsonArray.length() > 5) {
                        lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.DOCUMENT));
                    } else  {
                        lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP"), CellType.DOCUMENT));
                    }
                }

                final int length = Math.min(AlphaFineConstants.SHOW_SIZE, jsonArray.length());
                for (int i = 0; i < length; i++) {
                    DocumentModel cellModel = getDocumentModel(jsonArray, i);
                    this.lessModelList.add(cellModel);
                }
                for (int i = length; i < jsonArray.length(); i++) {
                    DocumentModel cellModel = getDocumentModel(jsonArray, i);
                    this.moreModelList.add(cellModel);
                }

            } catch (JSONException e) {
                FRLogger.getLogger().error(e.getMessage());
                return lessModelList;
            }

        }
        return lessModelList;
    }

    private DocumentModel getDocumentModel(JSONArray jsonArray, int i) throws JSONException {
        JSONObject object = jsonArray.getJSONObject(i);
        String name = (String) object.get("title");
        String content = ((String) object.get("summary"));
        String documentUrl = AlphaFineConstants.DOCUMENT_SEARCH_URL + object.get("did") + ".html";
        return new DocumentModel(name, content, documentUrl);
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return moreModelList;
    }

}
