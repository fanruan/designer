package com.fr.design.extra;

import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginsReaderFromStore {
    private static Set<Plugin> plugins = new HashSet<Plugin>();
    private static Set<Plugin> pluginsToUpdate = new HashSet<Plugin>();

    /**
     * 从插件商店服务器读取插件信息，以JSON形式返回
     *
     * @return 插件信息
     */
    public static Plugin[] readPlugins() throws Exception {
            String resText;
            try {
                HttpClient httpClient = new HttpClient(PluginConstants.PLUGIN_STORE_URL);
                resText = httpClient.getResponseText();
                String charSet = EncodeConstants.ENCODING_UTF_8;
                resText = URLDecoder.decode(URLDecoder.decode(resText, charSet), charSet);
            } catch (Exception e) {
                throw new Exception(Inter.getLocText("FR-Designer-Plugin_PluginMarket_Coding"));
            }
            if (StringUtils.isNotEmpty(resText)) {
                try {
                    plugins.clear();//先清空set
                    JSONArray jsonArray = new JSONArray(resText);
                    for (int i = 0, size = jsonArray.length(); i < size; i++) {
                        Plugin plugin = new Plugin();
                        plugin.parseJSON(jsonArray.optJSONObject(i));
                        if (plugin.isValidate()) {
                            plugins.add(plugin);
                        }
                    }
                } catch (JSONException e) {
                    throw new Exception(Inter.getLocText("FR-Designer-Plugin_Read_Plugin_List_Error"));
                }
            }

        return plugins.toArray(new Plugin[plugins.size()]);
    }

    /**
     * 从插件商店服务器读取插件信息，以JSON形式返回
     *
     * @return 插件信息
     */
    public static Plugin[] readPluginsForUpdate() throws Exception {
            String resText;
            try {
                HashMap<String, String> para = new HashMap<String, String>();
                para.put("plugins", PluginLoader.getLoader().pluginsToString());
                //只有当前设计器的jar高于插件新版本需要的jarTime时, 才提示更新该插件.
                para.put("jarTime", GeneralUtils.readBuildNO());
                HttpClient httpClient = new HttpClient(PluginConstants.PLUGIN_CHECK_UPDATE_URL, para);
                resText = httpClient.getResponseText();
                String charSet = EncodeConstants.ENCODING_UTF_8;
                resText = URLDecoder.decode(URLDecoder.decode(resText, charSet), charSet);
            } catch (Exception e) {
                throw new Exception(Inter.getLocText("FR-Designer-Plugin_PluginMarket_Coding"));
            }
            if (StringUtils.isNotEmpty(resText)) {
                try {
                    pluginsToUpdate.clear();
                    JSONArray jsonArray = new JSONArray(resText);
                    for (int i = 0, size = jsonArray.length(); i < size; i++) {
                        Plugin plugin = new Plugin();
                        plugin.parseJSON(jsonArray.optJSONObject(i));
                        if (plugin.isValidate()) {
                            pluginsToUpdate.add(plugin);
                        }
                    }
                } catch (JSONException e) {
                    throw new Exception(Inter.getLocText("FR-Designer-Plugin_Read_Plugin_List_Error"));
                }
            }

        return pluginsToUpdate.toArray(new Plugin[pluginsToUpdate.size()]);
    }

}