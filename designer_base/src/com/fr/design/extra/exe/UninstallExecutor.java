package com.fr.design.extra.exe;

import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by richie on 16/3/19.
 */
public class UninstallExecutor implements Executor {

    private String pluginInfo;
    private boolean isForce;
    private String result = "undo";

    public UninstallExecutor(String pluginInfo, boolean isForce) {
        this.pluginInfo = pluginInfo;
        this.isForce = isForce;
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
                        return null;
                    }

                    @Override
                    public void run(Process<String> process) {
                            PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
                            PluginManager.getController().uninstall(pluginMarker, isForce, new PluginTaskCallback() {
                                @Override
                                public void done(PluginTaskResult pluginTaskResult) {
                                    if (pluginTaskResult.isSuccess()) {
                                        result = "done";
                                        FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Delete_Success"));
                                        JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                                    } else {
                                        FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Delete_Failed"));
                                        JOptionPane.showMessageDialog(null, pluginTaskResult.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                    }
                }
        };
    }
}
