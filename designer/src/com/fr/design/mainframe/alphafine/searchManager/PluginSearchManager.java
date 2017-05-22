package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.cellModel.PluginModel;
import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.designer.other.HyperlinkMapFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class PluginSearchManager implements AlphaFineSearchProcessor {
    private static PluginSearchManager pluginSearchManager = null;
    private SearchResult lessModelList;
    private SearchResult moreModelList;

    public synchronized static PluginSearchManager getPluginSearchManager() {
        if (pluginSearchManager == null) {
            pluginSearchManager = new PluginSearchManager();
        }
        return pluginSearchManager;

    }

    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {

        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphafineConfigManager().isContainPlugin()) {
            String result;
            try {
                String encodedKey = URLEncoder.encode(searchText, "UTF-8");
                String url = AlphaFineConstants.PLUGIN_SEARCH_URL + "?keyword=" + encodedKey;
                HttpClient httpClient = new HttpClient(url);
                httpClient.setTimeout(5000);
                httpClient.asGet();
                result = httpClient.getResponseText();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = null;
                if (jsonObject.optJSONObject("result") != null) {
                    jsonArray = jsonObject.optJSONArray("result");
                }
                if (jsonArray != null && jsonArray.length() > 0) {
                    int length = Math.min(AlphaFineConstants.SHOW_SIZE, jsonArray.length());
                    for (int i = 0; i < length; i++) {
                        PluginModel cellModel = getPluginModel(jsonArray.optJSONObject(i), false);
                        this.lessModelList.add(cellModel);
                    }
                    for (int i = length; i < jsonArray.length(); i++) {
                        PluginModel cellModel = getPluginModel(jsonArray.optJSONObject(i), false);
                        this.moreModelList.add(cellModel);
                    }
                    if (jsonArray.length() > 0) {
                        if (jsonArray.length() > AlphaFineConstants.SHOW_SIZE) {
                            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.PLUGIN));
                        } else {
                            lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon"), CellType.PLUGIN));
                        }
                    }
                }

            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage());
                return lessModelList;
            }
        }
        return this.lessModelList;
    }

    private static PluginModel getPluginModel(JSONObject object, boolean isFromCloud) {
        String name = object.optString("name");
        String content = object.optString("description");
        int pluginId = object.optInt("id");
        String imageUrl = null;
        try {
            imageUrl = AlphaFineConstants.PLUGIN_IMAGE_URL + URLEncoder.encode(object.optString("pic").toString().substring(AlphaFineConstants.PLUGIN_IMAGE_URL.length()), "utf8");
        } catch (UnsupportedEncodingException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        String version = null;
        String jartime = null;
        CellType type;
        String link = object.optString("link");
        if (ComparatorUtils.equals(link, "plugin")) {
            version = isFromCloud? object.optString("pluginversion") : object.optString("version");
            jartime = object.optString("jartime");
            type = CellType.PLUGIN;
        } else {
            type = CellType.REUSE;
        }
        int price = object.optInt("price");
        return new PluginModel(name, content, imageUrl, version, jartime, type, price, pluginId);
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return this.moreModelList;
    }

    public static PluginModel getModelFromCloud(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
           return getPluginModel(jsonObject, true);
        }
        return null;
    }




}
