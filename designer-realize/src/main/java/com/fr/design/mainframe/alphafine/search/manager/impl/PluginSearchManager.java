package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.PluginModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.basic.version.Version;
import com.fr.plugin.basic.version.VersionIntervalFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class PluginSearchManager implements AlphaFineSearchProvider {
    private SearchResult lessModelList;
    private SearchResult moreModelList;

    private PluginSearchManager() {

    }

    public static PluginSearchManager getInstance() {
        return Holder.INSTANCE;

    }

    private static class Holder {
        private static final PluginSearchManager INSTANCE = new PluginSearchManager();
    }

    private static boolean isCompatibleCurrentEnv(String envVersion) {
        return VersionIntervalFactory.create(envVersion).contain(Version.currentEnvVersion());
    }

    private static PluginModel getPluginModel(JSONObject object, boolean isFromCloud) {
        String name = object.optString("name");
        String content = object.optString("description");
        String pluginId = object.optString("pluginid");
        String envVersion = object.optString("envversion");
        if (!isCompatibleCurrentEnv(envVersion)) {
            return null;
        }
        int id = object.optInt("id");
        int searchCount = object.optInt("searchCount");
        String imageUrl = null;
        try {
            imageUrl = isFromCloud ? AlphaFineConstants.PLUGIN_IMAGE_URL + URLEncoder.encode(object.optString("pic").toString().substring(AlphaFineConstants.PLUGIN_IMAGE_URL.length()), "utf8") : object.optString("pic");
        } catch (UnsupportedEncodingException e) {
            FineLoggerFactory.getLogger().error("plugin icon error: " + e.getMessage());
        }
        String version = null;
        String jartime = null;
        CellType type;
        String link = object.optString("link");
        if (ComparatorUtils.equals(link, "plugin")) {
            version = isFromCloud ? object.optString("pluginversion") : object.optString("version");
            jartime = object.optString("jartime");
            type = CellType.PLUGIN;
        } else {
            type = CellType.REUSE;
        }
        int price = object.optInt("price");
        return new PluginModel(name, content, imageUrl, version, jartime, link, pluginId, type, price, id, searchCount);
    }

    /**
     * 根据json获取对应的插件model
     *
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

    @Override
    public SearchResult getLessSearchResult(String[] searchText) {
        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        SearchResult searchResult = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainPlugin()) {
            if (ArrayUtils.isEmpty(searchText)) {
                lessModelList.add(new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Plugin_Addon")));
                return lessModelList;
            }
            SearchResult noConnectList = AlphaFineHelper.getNoConnectList(Holder.INSTANCE);
            if(noConnectList != null){
                return noConnectList;
            }
            for (int j = 0; j < searchText.length; j++) {
                try {
                    String encodedKey = URLEncoder.encode(searchText[j], EncodeConstants.ENCODING_UTF_8);
                    String url = AlphaFineConstants.PLUGIN_SEARCH_URL + "?keyword=" + encodedKey;
                    String result = HttpToolbox.get(url);
                    AlphaFineHelper.checkCancel();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("result");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AlphaFineHelper.checkCancel();
                            PluginModel cellModel = getPluginModel(jsonArray.optJSONObject(i), false);
                            if (cellModel != null && !AlphaFineHelper.getFilterResult().contains(cellModel) && !searchResult.contains(cellModel)) {
                                searchResult.add(cellModel);
                            }
                        }
                    }
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error("plugin search error :" + e.getMessage());
                }
            }
            if (searchResult.isEmpty()) {
                return this.lessModelList;
            } else if (searchResult.size() < AlphaFineConstants.SHOW_SIZE + 1) {
                lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Plugin_Addon")));
                lessModelList.addAll(searchResult);
            } else {
                lessModelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Plugin_Addon"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.PLUGIN));
                lessModelList.addAll(searchResult.subList(0, AlphaFineConstants.SHOW_SIZE));
                moreModelList.addAll(searchResult.subList(AlphaFineConstants.SHOW_SIZE, searchResult.size()));
            }
        }
        return this.lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return this.moreModelList;
    }
}
