package com.fr.design.extra;

import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.manage.PluginManager;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ibm on 2017/5/25.
 */
public class PluginReaderForDesigner {
    private static Set<String> pluginsToUpdate = new HashSet<String>();

    /**
     * 从插件商店服务器读取插件信息，以JSON形式返回
     *
     * @return 插件信息
     */
    public static String[] readPluginsForUpdate() throws Exception {
        String resText = null;
        String url = SiteCenter.getInstance().acquireUrlByKind("plugin.update");
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
        if (StringUtils.isNotEmpty(resText)) {
            try {
                pluginsToUpdate.clear();
                JSONArray jsonArray = new JSONArray(resText);
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jo = jsonArray.optJSONObject(i);
                    String id = jo.optString("id");
                    if(StringUtils.isNotEmpty(id)){
                        pluginsToUpdate.add(jo.optString("id", ""));
                    }
                }
            } catch (JSONException e) {
                throw new Exception(Inter.getLocText("FS-Web-Plugin_Read_Plugin_List_Error"));
            }
        }
        return pluginsToUpdate.toArray(new String[pluginsToUpdate.size()]);
    }
}
