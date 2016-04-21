package com.fr.design.extra.exe;

import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.http.HttpClient;
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

    public GetPluginFromStoreExecutor(String category, String seller, String fee) {
        this.category = category;
        this.seller = seller;
        this.fee = fee;
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
                        StringBuilder url = new StringBuilder(PluginWebBridge.PLUGIN_SHOP);
                        if (StringUtils.isNotBlank(category)) {
                            url.append("&cid=").append(category.split("-")[1]);
                        }
                        if (StringUtils.isNotBlank(seller)) {
                            url.append("&seller=").append(seller.split("-")[1]);
                        }
                        if (StringUtils.isNotBlank(fee)) {
                            url.append("&fee=").append(fee.split("-")[1]);
                        }
                        try {
                            HttpClient httpClient = new HttpClient(url.toString());
                            result = httpClient.getResponseText();
                        } catch (Exception e) {
                            FRLogger.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
