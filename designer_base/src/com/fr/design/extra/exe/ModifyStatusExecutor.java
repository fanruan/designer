package com.fr.design.extra.exe;

import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.stable.StringUtils;

import javax.swing.*;

/**
 * Created by richie on 16/3/19.
 */
public class ModifyStatusExecutor implements Executor {

    private String pluginInfo;
    private boolean active;
    private PluginContext plugin;

    public ModifyStatusExecutor(String pluginInfo) {
        this.pluginInfo = pluginInfo;
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
                    public void run(Process<String> process) {
                        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
                        plugin = PluginManager.getContext(pluginMarker);
                        active = !plugin.isActive();
                        if (active) {
                            PluginManager.getController().forbid(pluginMarker, new PluginTaskCallback() {
                                @Override
                                public void done(PluginTaskResult result) {
                                    if (result.isSuccess()) {
                                        JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Has_Been_Disabled"));
                                    } else {
                                        JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                        } else {
                            PluginManager.getController().enable(pluginMarker, new PluginTaskCallback() {
                                @Override
                                public void done(PluginTaskResult result) {
                                    if (result.isSuccess()) {
                                        JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Has_Been_Actived"));
                                    } else {
                                        JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                        }
                    }
                }
        };
    }
}
