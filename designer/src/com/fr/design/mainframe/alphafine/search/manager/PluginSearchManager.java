package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphafineContext;
import com.fr.design.actions.help.alphafine.AlphafineListener;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.NoResultModel;
import com.fr.design.mainframe.alphafine.cell.model.PluginModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class PluginSearchManager implements AlphaFineSearchProcessor {
    private static PluginSearchManager pluginSearchManager = null;

    private static final MoreModel titleModel = new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon"), CellType.PLUGIN);


    private SearchResult lessModelList;
    private SearchResult moreModelList;


    public synchronized static PluginSearchManager getPluginSearchManager() {
        if (pluginSearchManager == null) {
            pluginSearchManager = new PluginSearchManager();
        }
        return pluginSearchManager;

    }

    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
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
                if (!httpClient.isServerAlive()) {
                    return getNoConnectList();
                }
                result = httpClient.getResponseText();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                if (jsonArray != null) {
                    int length = Math.min(AlphaFineConstants.SHOW_SIZE, jsonArray.length());
                    for (int i = 0; i < length; i++) {
                        PluginModel cellModel = getPluginModel(jsonArray.optJSONObject(i), false);
                        this.lessModelList.add(cellModel);
                    }
                    for (int i = length; i < jsonArray.length(); i++) {
                        PluginModel cellModel = getPluginModel(jsonArray.optJSONObject(i), false);
                        this.moreModelList.add(cellModel);
                    }
                    if (jsonArray.length() > AlphaFineConstants.SHOW_SIZE) {
                        lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.PLUGIN));
                    } else {
                        lessModelList.add(0, titleModel);
                        if (lessModelList.size() == 1) {
                            lessModelList.add(AlphaFineHelper.noResultModel);
                        }
                    }

                } else {
                    return getNoResultList();
                }

            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage());
                return getNoResultList();
            }
        }
        return this.lessModelList;


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
        return new PluginModel(name, content, imageUrl, version, jartime, link, type, price, pluginId);
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return this.moreModelList;
    }

    /**
     * 根据json获取对应的插件model
     * @param object
     * @return
     */
    public static PluginModel getModelFromCloud(JSONObject object) {
        JSONObject jsonObject = object.optJSONObject("result");
        if (jsonObject != null) {
           return getPluginModel(jsonObject, true);
        } else {
            return getPluginModel(object, false);
        }

    }




}
