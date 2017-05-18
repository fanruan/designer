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
                if (jsonObject.get("result") != null) {
                    jsonArray = (JSONArray) jsonObject.get("result");
                }
                if (jsonArray.length() > 0) {
                    if (jsonArray.length() > AlphaFineConstants.SHOW_SIZE) {
                        lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.PLUGIN));
                    } else {
                        lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon"), CellType.PLUGIN));
                    }
                }

                int length = Math.min(5, jsonArray.length());
                for (int i = 0; i < length; i++) {
                    PluginModel cellModel = getPluginModel(jsonArray, i);
                    this.lessModelList.add(cellModel);
                }
                for (int i = length; i < jsonArray.length(); i++) {
                    PluginModel cellModel = getPluginModel(jsonArray, i);
                    this.moreModelList.add(cellModel);
                }
            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage());
                return lessModelList;
            }
        }
        return this.lessModelList;
    }

    private PluginModel getPluginModel(JSONArray jsonArray, int i) throws JSONException {
        JSONObject object = jsonArray.getJSONObject(i);
        String name = (String) object.get("name");
        String content = ((String) object.get("description"));
        String pluginUrl = AlphaFineConstants.REUSE_URL + object.get("id");
        String imageUrl = null;
        try {
            imageUrl = AlphaFineConstants.PLUGIN_IMAGE_URL + URLEncoder.encode(object.get("pic").toString().substring(AlphaFineConstants.PLUGIN_IMAGE_URL.length()), "utf8");
        } catch (UnsupportedEncodingException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        String version = null;
        String jartime = null;
        CellType type = CellType.REUSE;
        String link = (String) object.get("link");
        if (ComparatorUtils.equals(link, "plugin")) {
            version = (String) object.get("version");
            jartime = (String) object.get("jartime");
            type = CellType.PLUGIN;
            pluginUrl = AlphaFineConstants.PLUGIN_URL + object.get("id");
        }
        int price = (int) object.get("price");
        return new PluginModel(name, content, pluginUrl, imageUrl, version, jartime, type, price);
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return this.moreModelList;
    }




}
