package com.fr.design.extra.exe;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.extra.After;
import com.fr.design.extra.LoginCheckContext;
import com.fr.design.extra.PluginHelper;
import com.fr.design.extra.Process;
import com.fr.general.Inter;
import com.fr.plugin.PluginVerifyException;
import com.fr.plugin.dependence.PluginDependenceException;
import com.fr.stable.StringUtils;

import javax.swing.*;

/**
 * Created by richie on 16/3/19.
 */
public class InstallOnlineExecutor implements Executor {

    private String pluginID;

    public InstallOnlineExecutor(String pluginID) {
        this.pluginID = pluginID;
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
                        return "正在下载插件:" + pluginID;
                    }

                    @Override
                    public void run(final Process<String> process) {
                        //下载插件
                        if(StringUtils.isBlank(DesignerEnvManager.getEnvManager().getBBSName())){
                            LoginCheckContext.fireLoginCheckListener();
                        }
                        if(StringUtils.isNotBlank(DesignerEnvManager.getEnvManager().getBBSName())) {
                            String username = DesignerEnvManager.getEnvManager().getBBSName();
                            String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                            try {
                                PluginHelper.downloadPluginFile(pluginID, username, password, new Process<Double>() {
                                    @Override
                                    public void process(Double integer) {
                                        process.process(Math.round(integer * 100) + "%");
                                    }
                                });
                            } catch (Exception e) {
                                FRContext.getLogger().error(e.getMessage(), e);
                            }
                        }
                    }
                },
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在安装插件:" + pluginID;
                    }

                    @Override
                    public void run(Process<String> process) {
                        try {
                            PluginHelper.installPluginFromDisk(PluginHelper.getDownloadTempFile(), new After() {
                                @Override
                                public void done() {
                                    int rv = JOptionPane.showOptionDialog(
                                            null,
                                            Inter.getLocText("FR-Designer-Plugin_Install_Successful"),
                                            Inter.getLocText("FR-Designer-Plugin_Warning"),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"), Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")},
                                            null
                                    );
                                    if (rv == JOptionPane.OK_OPTION) {
                                        RestartHelper.restart();
                                    }
                                }
                            });
                        } catch (PluginVerifyException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                        } catch (PluginDependenceException e){
                            JOptionPane.showMessageDialog(null, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
    }
}
