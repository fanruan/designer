package com.fr.design.extra.exe;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.extra.After;
import com.fr.design.extra.LoginCheckContext;
import com.fr.design.extra.PluginHelper;
import com.fr.design.extra.Process;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.io.File;

/**
 * Created by richie on 16/3/19.
 */
public class UpdateOnlineExecutor implements Executor {

    private String[] pluginIDs;
    private static final int PERCENT_100 = 100;

    public UpdateOnlineExecutor(String[] pluginIDs) {
        this.pluginIDs = pluginIDs;
    }

    @Override
    public String getTaskFinishMessage() {
        return "task succeed";
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
                        if(StringUtils.isBlank(DesignerEnvManager.getEnvManager().getBBSName())){
                            LoginCheckContext.fireLoginCheckListener();
                        }
                        if(StringUtils.isNotBlank(DesignerEnvManager.getEnvManager().getBBSName())){
                            for (int i = 0; i < pluginIDs.length; i++) {
                                Plugin plugin = PluginLoader.getLoader().getPluginById(pluginIDs[i]);
                                String id = null;
                                if (plugin != null) {
                                    id = plugin.getId();
                                }
                                String username = DesignerEnvManager.getEnvManager().getBBSName();
                                String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                                try {
                                    PluginHelper.downloadPluginFile(id, username, password, new Process<Double>() {
                                        @Override
                                        public void process(Double integer) {
                                        }
                                    });
                                    updateFileFromDisk(PluginHelper.getDownloadTempFile());
                                    process.process(PERCENT_100 / pluginIDs.length * (i + 1) + "%");
                                } catch (Exception e) {
                                    FRContext.getLogger().error(e.getMessage(), e);
                                }
                            }
                            int rv = JOptionPane.showOptionDialog(
                                    null,
                                    Inter.getLocText("FR-Designer-Plugin_Update_Successful"),
                                    Inter.getLocText("FR-Designer-Plugin_Warning"),
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"),
                                            Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")
                                    },
                                    null
                            );
                            if (rv == JOptionPane.OK_OPTION) {
                                RestartHelper.restart();
                            }
                        }
                    }
                }
        };
    }

    private void updateFileFromDisk(File fileOnDisk) {
        try {
            Plugin plugin = PluginHelper.readPlugin(fileOnDisk);
            if (plugin == null) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Illegal_Plugin_Zip"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Plugin oldPlugin = PluginLoader.getLoader().getPluginById(plugin.getId());
            if (oldPlugin != null) {
                String[] files = PluginHelper.uninstallPlugin(FRContext.getCurrentEnv(), oldPlugin);
                PluginHelper.installPluginFromUnzippedTempDir(FRContext.getCurrentEnv(), plugin, new After() {
                    @Override
                    public void done() {
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Cannot_Update_Not_Install"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
