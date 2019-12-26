package com.fr.design.extra;

import com.fr.base.TemplateUtils;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.PluginVerifyException;
import com.fr.plugin.basic.version.Version;
import com.fr.plugin.basic.version.VersionIntervalFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginCoreErrorCode;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.view.PluginView;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ibm on 2017/5/25.
 */
public class PluginUtils {
    
    private static final String ERROR_CODE_I18N_PREFIX = "FR-Plugin_Error_";
    public static final String FR_VERSION = "fr_version";


    public static PluginMarker createPluginMarker(String pluginInfo) {
        //todo 判空
        String[] plugin = pluginInfo.split("_");
        PluginMarker pluginMarker = PluginMarker.create(plugin[0], plugin[1]);
        return pluginMarker;
    }

    public static JSONObject getLatestPluginInfo(String pluginID) throws Exception {
        String result = "";
        String plistUrl = CloudCenter.getInstance().acquireUrlByKind("plugin.searchAPI");
        if (StringUtils.isNotEmpty(plistUrl)) {
            StringBuilder url = new StringBuilder(plistUrl);
            if (StringUtils.isNotBlank(pluginID)) {
                url.append("?keyword=").append(pluginID);
            }
            try {
                HttpClient httpClient = new HttpClient(url.toString());
                httpClient.asGet();
                result = httpClient.getResponseText();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else {
            result = PluginConstants.CONNECTION_404;
        }
        JSONObject resultJSONObject = new JSONObject(result);
        JSONArray resultArr = resultJSONObject.getJSONArray("result");
        JSONObject latestPluginInfo = JSONObject.create();
        latestPluginInfo = (JSONObject) resultArr.get(0);
        return latestPluginInfo;
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

    public static boolean downloadShopScripts(String id, Process<Double> p) {
        InputStream reader = null;
        FileOutputStream writer = null;
        try {
            HttpClient httpClient = new HttpClient(getDownloadPath(id));
            if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int totalSize = httpClient.getContentLength();
                reader = httpClient.getResponseStream();
                String temp = StableUtils.pathJoin(PluginConstants.DOWNLOAD_PATH, PluginConstants.TEMP_FILE);
                StableUtils.makesureFileExist(new File(temp));
                writer = new FileOutputStream(temp);
                byte[] buffer = new byte[PluginConstants.BYTES_NUM];
                int bytesRead = 0;
                int totalBytesRead = 0;

                while ((bytesRead = reader.read(buffer)) > 0) {
                    writer.write(buffer, 0, bytesRead);
                    buffer = new byte[PluginConstants.BYTES_NUM];
                    totalBytesRead += bytesRead;
                    p.process(totalBytesRead / (double) totalSize);
                }
            } else {
                throw new com.fr.plugin.PluginVerifyException(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Connect_Server_Error"));
            }
        } catch (PluginVerifyException e) {
            FineJOptionPane.showMessageDialog(null, e.getMessage(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        } finally {
            closeStream(reader, writer);
        }
        return true;
    }

    private static void closeStream(InputStream reader, FileOutputStream writer){
        try {
            if (null != reader) {
                reader.close();
            }
            if (null != writer) {
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static String getDownloadPath(String id) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpClient httpClient = new HttpClient(CloudCenter.getInstance().acquireUrlByKind("shop.script.download")+ "?" + FR_VERSION + "=" + ProductConstants.VERSION);
        httpClient.asGet();
        String resText = httpClient.getResponseText();
        JSONObject resultJSONObject = new JSONObject(resText);
        String scriptUrl = resultJSONObject.optString("result");
        String charSet = EncodeConstants.ENCODING_UTF_8;
        scriptUrl = URLDecoder.decode(URLDecoder.decode(scriptUrl, charSet), charSet);

        return scriptUrl;
    }

    public static boolean isPluginMatch(PluginView pluginView, String text) {
        return StringUtils.contains(pluginView.getID(), text)
                || StringUtils.contains(pluginView.getName(), text)
                || StringUtils.contains(pluginView.getVersion(), text)
                || StringUtils.contains(pluginView.getEnvVersion(), text)
                || StringUtils.contains(pluginView.getVendor(), text)
                || StringUtils.contains(pluginView.getDescription(), text)
                || StringUtils.contains(pluginView.getChangeNotes(), text);

    }

    public static String pluginToHtml(PluginView pluginView) {
        String pluginName = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Plugin_Name");
        String pluginVersion = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Version");
        String startVersion = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Start_Version");
        String developer = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Developer");
        String desc = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Function_Description");
        String updateLog = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Plugin_Update_Log");
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
    
    public static String getMessageByErrorCode(PluginErrorCode errorCode) {
        if(errorCode == PluginCoreErrorCode.None){
            return "";
        }
        
        return com.fr.design.i18n.Toolkit.i18nCompatibleServerText(getInterKeyByErrorCode(errorCode));
    }
    
    private static String getInterKeyByErrorCode(PluginErrorCode errorCode) {
        
        return  errorCode.getDescription();
    }
    
    public static PluginMarker getInstalledPluginMarkerByID(String pluginID) {
        
        PluginContext context = PluginManager.getContext(pluginID);
        if (context != null) {
            return context.getMarker();
        }
        return null;
    }

    /**
     * 在不同设计器版本下展示不同插件
     * @return 插件
     */
    public static JSONArray filterPluginsFromVersion(JSONArray oriJSONArray) throws Exception{
        JSONArray resultJSONArray =  JSONArray.create();
        for(int i = 0; i < oriJSONArray.length(); i++){
            JSONObject jo = oriJSONArray.getJSONObject(i);
            String envVersion = jo.optString("envversion");
            if(isCompatibleCurrentEnv(envVersion)){
                resultJSONArray.put(jo);
            }
        }
        return resultJSONArray;
    }

    private static boolean isCompatibleCurrentEnv(String envVersion){
        return VersionIntervalFactory.create(envVersion).contain(Version.currentEnvVersion());
    }


    public static JSONArray transferStorePluginToJson(PluginContext [] pluginContexts){
        JSONArray ja = JSONArray.create();
        try {
            for(PluginContext pluginContext : pluginContexts){
                JSONObject jo = JSONObject.create();
                jo.put("id", pluginContext.getID());
                jo.put("name", pluginContext.getName());
                jo.put("version", pluginContext.getVersion());
                jo.put("envVersion", pluginContext.getEnvVersion());
                jo.put("description", pluginContext.getDescription());
                jo.put("changeNotes", pluginContext.getChangeNotes());
                jo.put("vendor", pluginContext.getVendor());
                jo.put("price", pluginContext.getPrice());
                jo.put("requiredJarTime", pluginContext.getRequiredJarTime());
                jo.put("active", pluginContext.isActive());
                jo.put("hidden", pluginContext.isHidden());
                jo.put("free", pluginContext.isFree());
                jo.put("licDamaged", pluginContext.isLicDamaged());
                jo.put("available", pluginContext.isAvailable());
                jo.put("leftDays", pluginContext.getLeftDays());
                jo.put("onTrial", pluginContext.isOnTrial());
                jo.put("deadline", getDeadline(pluginContext));
                jo.put("registerFailed", pluginContext.isRegisterFailed());
                jo.put("selfState", pluginContext.getSelfState());
                jo.put("switchedReason", pluginContext.getSwitchedReason());
                ja.put(jo);
            }
        }catch (Exception e){
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return ja;
    }


    private static String getDeadline(PluginContext plugin) {

        int leftDays = plugin.getLeftDays();
        if (leftDays == Integer.MAX_VALUE) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Store_Permanent");
        }
        Calendar deadline = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        deadline.add(Calendar.DATE, leftDays);
        return format.format(deadline.getTime());
    }

}
