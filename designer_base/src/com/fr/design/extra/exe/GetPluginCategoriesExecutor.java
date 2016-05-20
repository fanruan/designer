package com.fr.design.extra.exe;

import com.fr.design.extra.Process;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;

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
                        HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("plugin.category"));
                        result = httpClient.getResponseText();
                    }
                }
        };
    }
}
