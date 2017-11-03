package com.fr.design.extra.exe;

import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;

/**
 * Created by vito on 16/4/18.
 */
public class SearchOnlineExecutor implements Executor {
    private String result;
    private String keyword;

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
                            HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("plugin.plist") + "&keyword=" + keyword);
                            result = httpClient.getResponseText();

                        } catch (Exception e) {
                            FRLogger.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
