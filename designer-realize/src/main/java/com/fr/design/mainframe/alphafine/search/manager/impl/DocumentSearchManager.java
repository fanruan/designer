package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.DocumentModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.component.AlphaFineDialog;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;

import java.io.IOException;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class DocumentSearchManager implements AlphaFineSearchProvider {
    private static volatile DocumentSearchManager instance;
    private SearchResult lessModelList;
    private SearchResult moreModelList;

    public static DocumentSearchManager getInstance() {
        if (instance == null) {
            synchronized (DocumentSearchManager.class) {
                if (instance == null) {
                    instance = new DocumentSearchManager();
                }
            }
        }
        return instance;
    }

    /**
     * 根据json信息获取文档model
     *
     * @param object
     * @return
     */
    public static DocumentModel getModelFromCloud(JSONObject object) {
        String name = object.optString("title");
        String content = object.optString("summary");
        int documentId = object.optInt("did");
        int searchCount = object.optInt("searchCount");
        return new DocumentModel(name, content, documentId, searchCount);
    }

    @Override
    public SearchResult getLessSearchResult(String[] searchText) {
        if (ArrayUtils.isEmpty(searchText)) {
            return new SearchResult();
        } else if (AlphaFineDialog.data == null) {
            return AlphaFineHelper.getNoConnectList(instance);
        }
        lessModelList = new SearchResult();
        moreModelList = new SearchResult();
        if (ArrayUtils.isEmpty(searchText)) {
            lessModelList.add(new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Community_Help")));
            return lessModelList;
        }
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainDocument()) {
            SearchResult searchResult = new SearchResult();
            for (int j = 0; j < searchText.length; j++) {
                String url = AlphaFineConstants.DOCUMENT_SEARCH_URL + searchText[j] + AlphaFineConstants.FIRST_PAGE;
                try {
                    String result = HttpToolbox.get(url);
                    AlphaFineHelper.checkCancel();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("docdata");
                    if (jsonArray != null) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            AlphaFineHelper.checkCancel();
                            DocumentModel cellModel = getModelFromCloud(jsonArray.optJSONObject(i));
                            if (!AlphaFineHelper.getFilterResult().contains(cellModel)) {
                                searchResult.add(cellModel);
                            }
                        }
                    }
                } catch (JSONException e) {
                    FineLoggerFactory.getLogger().error("document search error: " + e.getMessage());
                } catch (IOException e) {
                    FineLoggerFactory.getLogger().error("document search get result error: " + e.getMessage());
                }
            }
            lessModelList.clear();
            moreModelList.clear();
            if (searchResult.isEmpty()) {
                return lessModelList;
            } else if (searchResult.size() < AlphaFineConstants.SHOW_SIZE + 1) {
                lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Community_Help")));
                lessModelList.addAll(searchResult);
            } else {
                lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Community_Help"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.DOCUMENT));
                lessModelList.addAll(searchResult.subList(0, AlphaFineConstants.SHOW_SIZE));
                moreModelList.addAll(searchResult.subList(AlphaFineConstants.SHOW_SIZE, searchResult.size()));
            }
        }
        return lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return moreModelList;
    }

}
