package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.json.JSONTokener;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author alex.sung created on 2018/7/23.
 */
public class ComplementAdviceManager {

    public static int count = 0;

    private static ComplementAdviceManager instance;
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
    public synchronized SearchResult getAllSearchResult(String[] searchText) {
        allModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
            SearchResult searchResult = new SearchResult();
            for (int j = 0; j < searchText.length; j++) {
                String result;
                String token = DigestUtils.md5Hex(AlphaFineConstants.ALPHA_ROBOT_SEARCH_TOKEN + searchText[j]);
                String url = AlphaFineConstants.COMPLEMENT_ADVICE_SEARCH_URL_PREFIX + "msg=" + searchText[j] + "&token=" + token;
                HttpClient httpClient = new HttpClient(url);
                httpClient.asGet();
                result = httpClient.getResponseText();
                AlphaFineHelper.checkCancel();
                try {
                    Object json = new JSONTokener(result).nextValue();
                    if (json instanceof JSONArray) {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AlphaFineHelper.checkCancel();
                            JSONObject jsonObject = jsonArray.optJSONObject(i);

                            String temp = jsonObject.optString("keywords");
                            if (!StringUtils.isEmpty(temp)) {
                                RobotModel robotModel = new RobotModel(temp, null);
                                if (!AlphaFineHelper.getFilterResult().contains(robotModel) && !allModelList.contains(robotModel)) {
                                    allModelList.add(robotModel);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    FineLoggerFactory.getLogger().error("complement advice search error: " + e.getMessage());
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
