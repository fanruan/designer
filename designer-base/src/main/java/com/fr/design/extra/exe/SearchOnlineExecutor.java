package com.fr.design.extra.exe;

import com.fr.design.extra.PluginOperateUtils;
import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

/**
 * Created by vito on 16/4/18.
 */
public class SearchOnlineExecutor implements Executor {
    private String result = JSONArray.create().toString();
    private final String keyword;

    public SearchOnlineExecutor(String keyword) {
        this.keyword = keyword;
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
                        try {
                            if (StringUtils.isBlank(keyword)) {
                                result = PluginOperateUtils.getRecommendPlugins();
                                return;
                            }
                            String url = CloudCenter.getInstance().acquireUrlByKind("shop.plugin.store");
                            if (StringUtils.isEmpty(url)) {
                                return;
                            }
                            HttpClient httpClient = new HttpClient(url + "&keyword=" + keyword);
                            httpClient.asGet();
                            String responseText = httpClient.getResponseText();
                            JSONObject jsonObject = new JSONObject(responseText);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONArray resultJSONArray = PluginUtils.filterPluginsFromVersion(jsonArray);
                            result = resultJSONArray.toString();
                        } catch (Exception e) {
                            FineLoggerFactory.getLogger().error(e.getMessage(), e);
                        }
                    }
                }
        };
    }
}
