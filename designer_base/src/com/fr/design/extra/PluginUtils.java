package com.fr.design.extra;

import com.fr.base.TemplateUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;

import com.fr.plugin.view.PluginView;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ibm on 2017/5/25.
 */
public class PluginUtils {


    public static PluginMarker createPluginMarker(String pluginInfo) {
        //todo 判空
        String[] plugin = pluginInfo.split("_");
        PluginMarker pluginMarker = PluginMarker.create(plugin[0], plugin[1]);
        return pluginMarker;
    }

    public static String getLatestPluginInfo(String pluginID){
        String result = "";
        String plistUrl = SiteCenter.getInstance().acquireUrlByKind("plugin.searchAPI");
        if (StringUtils.isNotEmpty(plistUrl)) {
            StringBuilder url = new StringBuilder(plistUrl);
            if (StringUtils.isNotBlank(pluginID)) {
                url.append("?keyword=").append(pluginID);
            }
            try {
                HttpClient httpClient = new HttpClient(url.toString());
                result = httpClient.getResponseText();
            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage());
            }
        } else {
            result = PluginConstants.CONNECTION_404;
        }
        return result;
    }

    public static String transPluginsToString(List<PluginContext> plugins) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (PluginContext plugin : plugins) {
            JSONObject jo = new JSONObject();
            jo.put("id", plugin.getID());
            jo.put("version", plugin.getVersion());
            jsonArray.put(jo);
        }
        return jsonArray.toString();
    }

    public static void downloadShopScripts(String id, String username, String password, Process<Double> p) throws Exception{
        HttpClient httpClient = new HttpClient(getDownloadPath(id, username, password));
        if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
            int totalSize = httpClient.getContentLength();
            InputStream reader = httpClient.getResponseStream();
            String temp = StableUtils.pathJoin(PluginConstants.DOWNLOAD_PATH, PluginConstants.TEMP_FILE);
            StableUtils.makesureFileExist(new File(temp));
            FileOutputStream writer = new FileOutputStream(temp);
            byte[] buffer = new byte[PluginConstants.BYTES_NUM];
            int bytesRead = 0;
            int totalBytesRead = 0;

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[PluginConstants.BYTES_NUM];
                totalBytesRead += bytesRead;
                p.process(totalBytesRead / (double) totalSize);
            }
            reader.close();
            writer.flush();
            writer.close();
        } else {
            throw new com.fr.plugin.PluginVerifyException(Inter.getLocText("FR-Designer-Plugin_Connect_Server_Error"));
        }
    }

    private static String getDownloadPath(String id, String username, String password) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("username", username);
        map.put("password", password);
        HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("plugin.download"), map);
        String resText = httpClient.getResponseText();
        String charSet = EncodeConstants.ENCODING_UTF_8;
        resText = URLDecoder.decode(URLDecoder.decode(resText, charSet), charSet);

        return resText;
    }

    public static boolean isPluginMatch(PluginView pluginView, String text){
        return StringUtils.contains(pluginView.getID(), text)
                || StringUtils.contains(pluginView.getName(), text)
                || StringUtils.contains(pluginView.getVersion(), text)
                || StringUtils.contains(pluginView.getEnvVersion(), text)
                || StringUtils.contains(pluginView.getVendor(), text)
                || StringUtils.contains(pluginView.getDescription(), text)
                || StringUtils.contains(pluginView.getChangeNotes(), text);

    }

    public static String pluginToHtml(PluginView pluginView){
        String pluginName = Inter.getLocText("FR-Plugin-Plugin_Name");
        String pluginVersion = Inter.getLocText("FR-Plugin-Plugin_Version");
        String startVersion = Inter.getLocText("FR-Plugin-Start_Version");
        String developer = Inter.getLocText("FR-Plugin_Developer");
        String desc = Inter.getLocText("FR-Plugin-Function_Description");
        String updateLog = Inter.getLocText("FR-Plugin-Update_Log");
        Map<String, String> map = new HashMap<String, String>();

        map.put("name", pluginName);
        map.put("name_value", pluginView.getName());

        map.put("version", pluginVersion);
        map.put("version_value", pluginView.getVersion());

        map.put("env", startVersion);
        map.put("env_value", pluginView.getEnvVersion());

        map.put("dev", developer);
        map.put("dev_value", pluginView.getVendor());

        map.put("fun", desc);
        map.put("fun_value", pluginView.getDescription());

        map.put("update", updateLog);
        map.put("update_value", pluginView.getDescription());

        try {
            return TemplateUtils.renderTemplate("/com/fr/plugin/plugin.html", map);
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
    }

}
