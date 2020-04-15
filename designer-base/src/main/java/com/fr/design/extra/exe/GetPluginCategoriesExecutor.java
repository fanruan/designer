package com.fr.design.extra.exe;

import com.fr.design.extra.PluginConstants;
import com.fr.design.extra.Process;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;

/**
 * Created by vito on 16/5/16.
 */
public class GetPluginCategoriesExecutor implements Executor {
    private String result = "[]";

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
                        return null;
                    }

                    @Override
                    public void run(Process<String> process) {
                        String url = CloudCenter.getInstance().acquireUrlByKind("shop.plugin.category");
                        if (StringUtils.isNotEmpty(url)) {
                            HttpClient httpClient = new HttpClient(url);
                            result = httpClient.getResponseText();
                        } else {
                            result = PluginConstants.CONNECTION_404;
                        }

                    }
                }
        };
    }
}
