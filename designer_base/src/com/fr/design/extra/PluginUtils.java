package com.fr.design.extra;

import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;

import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

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

}
