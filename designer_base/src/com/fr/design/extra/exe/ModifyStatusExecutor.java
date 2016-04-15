package com.fr.design.extra.exe;

import com.fr.base.FRContext;
import com.fr.design.extra.PluginWebBridge;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.StringUtils;

/**
 * Created by richie on 16/3/19.
 */
public class ModifyStatusExecutor implements Executor {

    private String pluginID;
    private boolean active;
    private Plugin plugin;

    public ModifyStatusExecutor(String pluginID) {
        this.pluginID = pluginID;
    }

    @Override
    public String getTaskFinishMessage() {
        return plugin.isActive() ? Inter.getLocText("FR-Designer-Plugin_Has_Been_Actived") : Inter.getLocText("FR-Designer-Plugin_Has_Been_Disabled");
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
                    public void run() {
                        plugin = PluginLoader.getLoader().getPluginById(pluginID);
                        active = !plugin.isActive();
                        plugin.setActive(active);
                        try {
                            FRContext.getCurrentEnv().writePlugin(plugin);
                            PluginWebBridge.getHelper().showRestartMessage(plugin.isActive() ? Inter.getLocText("FR-Designer-Plugin_Has_Been_Actived") : Inter.getLocText("FR-Designer-Plugin_Has_Been_Disabled"));
                        } catch (Exception e) {
                            FRContext.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
