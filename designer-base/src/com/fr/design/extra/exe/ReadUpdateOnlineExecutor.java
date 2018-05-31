package com.fr.design.extra.exe;

import com.fr.design.extra.PluginsReaderFromStore;
import com.fr.design.extra.Process;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.view.PluginView;
import com.fr.stable.StringUtils;

import java.util.List;

/**
 * Created by vito on 16/4/19.
 */
public class ReadUpdateOnlineExecutor implements Executor {
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
                        return StringUtils.EMPTY;
                    }

                    @Override
                    public void run(Process<String> process) {
                        try {
                            List<PluginView> plugins = PluginsReaderFromStore.readPluginsForUpdate();
                            JSONArray jsonArray = new JSONArray();
                            for (PluginView plugin : plugins) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("pluginid", plugin.getID());
                                jsonArray.put(jsonObject);
                            }
                            result = jsonArray.toString();
                        } catch (Exception e) {
                            FineLoggerFactory.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
