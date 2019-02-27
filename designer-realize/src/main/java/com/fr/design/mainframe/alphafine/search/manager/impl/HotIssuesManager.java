package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSON;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONFactory;
import com.fr.json.JSONObject;
import com.fr.json.JSONUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by alex.sung on 2018/8/3.
 */
public class HotIssuesManager {
    private static volatile HotIssuesManager instance;
    private static final int HOT_ITEM_NUM = 6;
    private static final int HOT_SUB_ITEM_NUM = 4;
    private static final String HOT_ITEM = "item";
    private static final String HOT_ITEM_DATA = "itemData";
    private static final String HOT_ITEM_TEXT = "text";
    private static final String HOT_TYPE = "type";
    private static final String HOT_DATA = "data";

    public static HotIssuesManager getInstance() {
        if (instance == null) {
            synchronized (HotIssuesManager.class) {
                if (instance == null) {
                    instance = new HotIssuesManager();
                }
            }
        }
        return instance;
    }

    String[][] data = new String[HOT_ITEM_NUM][];
    Map<String, List> map = new HashMap<>();

    /**
     * 将子标题下的数据塞入modeList
     * @param subTitle
     * @return
     */
    public SearchResult getTitleSearchResult(String subTitle) {
        SearchResult modeList = new SearchResult();
        modeList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText((subTitle))));

        List<String> issueList = map.get(subTitle);
        for (int i = 0; i < issueList.size(); i++) {
            RobotModel robotModel = new RobotModel(issueList.get(i), null);
            robotModel.setHotItemModel(true);
            modeList.add(robotModel);
        }
        return modeList;
    }

    /**
     * 从热门问题接口获取热门问题
     * @return
     */
    public String[][] getHotIssues() {

        try {
            String result = HttpToolbox.get(AlphaFineConstants.ALPHA_HOT_SEARCH);
            JSONArray jsonArray = JSONFactory.createJSON(JSON.ARRAY, result);
            if(jsonArray != null){
                for (int i = 0; i < HOT_ITEM_NUM; i++) {
                    AlphaFineHelper.checkCancel();
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    data[i] = getTitleStrings(jsonObject);
                }
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("hotissues search error: " + e.getMessage());
            return null;
        }
        return data;
    }

    /**
     * 根据子标题获取该标题下数据
     * @param jsonObject
     * @return
     */
    private String[] getTitleStrings(JSONObject jsonObject) {
        String[] temp = getSubTitleFromCloud(jsonObject.optJSONObject(HOT_DATA));
        String[] temp1 = new String[1];
        temp1[0] = jsonObject.optString(HOT_TYPE);
        int strLen1 = temp.length;
        int strLen2 = temp1.length;

        temp1 = Arrays.copyOf(temp1, strLen2 + strLen1);
        System.arraycopy(temp, 0, temp1, strLen2, strLen1);

        getIssueStrings(jsonObject.optJSONObject(HOT_DATA));

        return temp1;
    }

    /**
     * 获取子标题和子标题下的问题列表
     * @param data
     * @return
     */
    private void getIssueStrings(JSONObject data) {
        try {
            for (int j = 0; j < HOT_SUB_ITEM_NUM; j++) {
                String temp = data.getString(HOT_ITEM + (j + 1));
                JSONArray jsonArray = data.getJSONArray(HOT_ITEM_DATA + (j + 1));
                List<String> tempList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    tempList.add(jsonArray.optJSONObject(i).optString(HOT_ITEM_TEXT));
                }
                map.put(temp, tempList);
            }

        } catch (JSONException e) {
        }
    }

    /**
     * 获取问题列表
     * @param data
     * @return
     */
    private String[] getSubTitleFromCloud(JSONObject data) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < HOT_SUB_ITEM_NUM; i++) {
            String temp = data.optString(HOT_ITEM + (i + 1));
            if (!StringUtils.isEmpty(temp)) {
                list.add(temp);
            }
        }
        String[] strings = new String[list.size()];
        list.toArray(strings);
        return strings;
    }
}
