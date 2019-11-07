package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.third.org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by alex.sung on 2018/8/3.
 */
public class SimilarSearchManager implements AlphaFineSearchProvider {
    private SearchResult lessModelList;
    private SearchResult moreModelList = new SearchResult();

    private SimilarSearchManager() {

    }

    public static SimilarSearchManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final SimilarSearchManager INSTANCE = new SimilarSearchManager();
    }

    @Override
    public SearchResult getLessSearchResult(String[] searchText) {
        lessModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
            if (ArrayUtils.isEmpty(searchText)) {
                return new SearchResult();
            }
            SearchResult noConnectList = AlphaFineHelper.getNoConnectList(Holder.INSTANCE);
            if (noConnectList != null) {
                return noConnectList;
            }
            SearchResult allModelList = new SearchResult();
            for (int j = 0; j < searchText.length; j++) {
                String token = DigestUtils.md5Hex(AlphaFineConstants.ALPHA_ROBOT_SEARCH_TOKEN + searchText[j]);
                String url = AlphaFineConstants.SIMILAR_SEARCH_URL_PREFIX + "msg=" + searchText[j] + "&token=" + token;
                try {
                    String result = HttpToolbox.get(url);
                    AlphaFineHelper.checkCancel();
                    allModelList = AlphaFineHelper.getModelListFromJSONArray(result, "title");
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().debug("similar search error.search str {}", searchText[j]);
                }
            }
            moreModelList.clear();
            if (allModelList.isEmpty()) {
                return lessModelList;
            } else if (allModelList.size() < AlphaFineConstants.SHOW_SIZE + 1) {
                lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Relation_Item")));
                lessModelList.addAll(allModelList);
            } else {
                lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Relation_Item"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.ROBOT));
                lessModelList.addAll(allModelList.subList(0, AlphaFineConstants.SHOW_SIZE));
                moreModelList.addAll(allModelList.subList(AlphaFineConstants.SHOW_SIZE, allModelList.size()));
            }
        }
        return lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return moreModelList;
    }

    /**
     * 根据json信息获取RobotModel
     *
     * @param object
     * @return
     */
    public static RobotModel getModelFromCloud(JSONObject object) {
        String name = object.optString("title");
        String content = object.optString("content");
        return new RobotModel(name, content);
    }
}
