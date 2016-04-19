package com.fr.design.extra.exe;

import com.fr.design.extra.PluginsReaderFromStore;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.plugin.Plugin;
import com.fr.stable.StringUtils;

/**
 * Created by vito on 16/4/19.
 */
public class ReadUpdateOnlineExecutor implements Executor {
    private Plugin[] plugins;
    private String result;

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
                            plugins = PluginsReaderFromStore.readPluginsForUpdate();
                            StringBuilder sb = new StringBuilder();
                            if (plugins != null) {
                                sb.append("[");
                                for (Plugin plugin : plugins) {
                                    sb.append("{pluginid:'").append(plugin.getId()).append("'}");
                                }
                                sb.append("]");
                            }
                            result = sb.toString();
                        } catch (Exception e) {
                            FRLogger.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
