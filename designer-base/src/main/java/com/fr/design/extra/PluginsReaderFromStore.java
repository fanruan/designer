package com.fr.design.extra;

import com.fr.general.CloudCenter;
import com.fr.general.GeneralUtils;

import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.view.PluginView;
import com.fr.plugin.view.PluginViewReader;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;

import java.net.URLDecoder;
import java.util.*;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginsReaderFromStore {
    private static Set<PluginView> plugins = new HashSet<PluginView>();
    private static Set<PluginView> pluginsToUpdate = new HashSet<PluginView>();

    /**
     * 从插件商店服务器读取插件信息，以JSON形式返回
     *
     * @return 插件信息
     */
    public static List<PluginView> readPlugins() throws Exception {
        String resText;
        try {
            HttpClient httpClient = new HttpClient(CloudCenter.getInstance().acquireUrlByKind("plugin.store"));
            resText = httpClient.getResponseText();
            String charSet = EncodeConstants.ENCODING_UTF_8;
            resText = URLDecoder.decode(URLDecoder.decode(resText, charSet), charSet);
        } catch (Exception e) {
            throw new Exception(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_PluginMarket_Coding"));
        }
        return parseResText(resText, plugins);
    }

    /**
     * 从插件商店服务器读取插件信息，以JSON形式返回
     *
     * @return 插件信息
     */
    public static List<PluginView> readPluginsForUpdate() throws Exception {
        String resText = null;
        String url = CloudCenter.getInstance().acquireUrlByKind("plugin.update");
        if (StringUtils.isNotEmpty(url)) {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("plugins", PluginUtils.transPluginsToString(PluginManager.getContexts()));
            //只有当前设计器的jar高于插件新版本需要的jarTime时, 才提示更新该插件.
            para.put("jarTime", GeneralUtils.readBuildNO());
            HttpClient httpClient = new HttpClient(url, para);
            resText = httpClient.getResponseText();
            String charSet = EncodeConstants.ENCODING_UTF_8;
            resText = URLDecoder.decode(URLDecoder.decode(resText, charSet), charSet);
        }
        return parseResText(resText, pluginsToUpdate);
    }

    private static List<PluginView> parseResText(String resText, Set<PluginView> plugins) throws Exception {
        if (StringUtils.isNotEmpty(resText)) {
            try {
                plugins.clear();
                JSONArray jsonArray = new JSONArray(resText);
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    PluginView pluginView = PluginViewReader.readFromJson(jsonArray.optJSONObject(i));
                    if (PluginOperateUtils.pluginValidate(pluginView)) {
                        plugins.add(pluginView);
                    }
                }
            } catch (JSONException e) {
                throw new Exception(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_Read_Plugin_List_Error"));
            }
        }
        return new ArrayList<>(plugins);
    }

}