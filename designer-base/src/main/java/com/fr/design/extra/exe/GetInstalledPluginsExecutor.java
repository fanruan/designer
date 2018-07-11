package com.fr.design.extra.exe;

import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.json.JSONArray;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginManager;

import java.util.List;

/**
 * @author kerry
 * @date 2018/6/1
 */
public class GetInstalledPluginsExecutor  implements Executor {
    private String result ="[]";
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
                        List<PluginContext> plugins = PluginManager.getContexts();
                        JSONArray ja = PluginUtils.transferStorePluginToJson(plugins.toArray(new PluginContext[plugins.size()]));
                        result = ja.toString();
                    }
                }
        };
    }
}
