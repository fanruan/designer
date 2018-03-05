package com.fr.design.extra.exe;

import com.fr.design.extra.PluginConstants;
import com.fr.design.extra.PluginOperateUtils;
import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;

/**
 * Created by vito on 16/4/18.
 * 获取插件分类信息
 */
public class GetPluginFromStoreExecutor implements Executor {
    private String result = "[]";
    private String category;
    private String seller;
    private String fee;
    private String scope;

    public GetPluginFromStoreExecutor(JSONObject info) {
        this.category = info.optString("categories");
        this.fee = info.optString("fee");
        this.seller = info.optString("seller");
        this.scope = info.optString("scope");
    }

    public GetPluginFromStoreExecutor(String category, String seller, String fee, String scope) {
        this.category = category;
        this.seller = seller;
        this.fee = fee;
        this.scope = scope;
    }

    @Override
    public String getTaskFinishMessage() {
        return result;
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return StringUtils.EMPTY;
                    }

                    @Override
                    public void run(Process<String> process) {
                        String plistUrl = SiteCenter.getInstance().acquireUrlByKind("shop.plugin.plist") + "?";
                        boolean getRecommend = StringUtils.isEmpty(category) && StringUtils.isEmpty(seller) && StringUtils.isEmpty(fee) && StringUtils.isEmpty(scope);
                        if (getRecommend) {
                            result = PluginOperateUtils.getRecommendPlugins();
                            return;
                        }

                        if (StringUtils.isNotBlank(plistUrl)) {
                            StringBuilder url = new StringBuilder();
                            url.append(plistUrl);
                            PluginOperateUtils.dealParams(url, category, seller, fee, scope);
                            try {
                                HttpClient httpClient = new HttpClient(url.toString());
                                httpClient.asGet();
                                String responseText = httpClient.getResponseText();
                                JSONObject resultJSONObject = new JSONObject(responseText);
                                JSONArray resultArr = resultJSONObject.getJSONArray("result");
                                JSONArray resultJSONArray = PluginUtils.filterPluginsFromVersion(resultArr);
                                result = resultJSONArray.toString();
                            } catch (Exception e) {
                                FRLogger.getLogger().error(e.getMessage());
                            }
                        } else {
                            result = PluginConstants.CONNECTION_404;
                        }
                    }
                }
        };
    }
}
