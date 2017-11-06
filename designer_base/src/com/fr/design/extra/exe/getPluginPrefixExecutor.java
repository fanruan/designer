package com.fr.design.extra.exe;

import com.fr.design.extra.Process;
import com.fr.general.SiteCenter;
import com.fr.stable.StringUtils;

/**
 * Created by kerry on 2017/11/3.
 */
public class getPluginPrefixExecutor implements Executor {
    private String result = StringUtils.EMPTY;

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
                        result = SiteCenter.getInstance().acquireUrlByKind("plugin.url.prefix");
                    }
                }
        };
    }
}

