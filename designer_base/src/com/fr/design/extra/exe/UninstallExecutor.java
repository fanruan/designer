package com.fr.design.extra.exe;

import com.fr.base.FRContext;
import com.fr.design.RestartHelper;
import com.fr.design.extra.PluginHelper;
import com.fr.design.extra.Process;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;

import javax.swing.*;

/**
 * Created by richie on 16/3/19.
 */
public class UninstallExecutor implements Executor {

    private String[] pluginIDs;
    private String result = "undo";

    public UninstallExecutor(String[] pluginIDs) {
        this.pluginIDs = pluginIDs;
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
                        int rv = JOptionPane.showOptionDialog(
                                null,
                                Inter.getLocText("FR-Designer-Plugin_Will_Be_Delete"),
                                Inter.getLocText("FR-Designer-Plugin_Warning"),
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"),
                                        Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later"),
                                        Inter.getLocText("FR-Designer-Basic_Cancel")
                                },
                                null
                        );
                        if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                            return;
                        }
                        for (String pluginID : pluginIDs) {
                            try {
                                Plugin plugin = PluginLoader.getLoader().getPluginById(pluginID);
                                String[] filesToBeDelete = PluginHelper.uninstallPlugin(FRContext.getCurrentEnv(), plugin);
                                RestartHelper.saveFilesWhichToDelete(filesToBeDelete);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        result = "done";
                        if (rv == JOptionPane.OK_OPTION) {
                            RestartHelper.restart();
                        }
                    }
                }
        };
    }
}
