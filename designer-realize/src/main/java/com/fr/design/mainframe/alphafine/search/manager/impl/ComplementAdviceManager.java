package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.http.HttpToolbox;
import com.fr.log.FineLoggerFactory;
import com.fr.third.org.apache.commons.codec.digest.DigestUtils;


/**
 * @author alex.sung created on 2018/7/23.
 */
public class ComplementAdviceManager {

    private static volatile ComplementAdviceManager instance;
    private SearchResult allModelList;

    public static ComplementAdviceManager getInstance() {
        if (instance == null) {
            synchronized (ComplementAdviceManager.class) {
                if (instance == null) {
                    instance = new ComplementAdviceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 从接口中获取补全建议结果
     * @param searchText
     * @return
     */
    public SearchResult getAllSearchResult(String[] searchText) {
        allModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
            SearchResult searchResult = new SearchResult();
            for (int j = 0; j < searchText.length; j++) {
                String token = DigestUtils.md5Hex(AlphaFineConstants.ALPHA_ROBOT_SEARCH_TOKEN + searchText[j]);
                String url = AlphaFineConstants.COMPLEMENT_ADVICE_SEARCH_URL_PREFIX + "msg=" + searchText[j] + "&token=" + token;
                try {
                    String result = HttpToolbox.get(url);
                    AlphaFineHelper.checkCancel();
                    allModelList = AlphaFineHelper.getModelListFromJSONArray(result,"keywords");
                } catch(Exception e){
                    FineLoggerFactory.getLogger().debug("complement advice search error: " + e.getMessage());
                }
            }
            if (searchResult.isEmpty()) {
                return allModelList;
            } else {
                allModelList.addAll(searchResult);
            }
        }
        return allModelList;
    }
}
