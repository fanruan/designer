package com.fr.design.extra.exe;

import com.fr.design.extra.After;
import com.fr.design.extra.PluginHelper;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.PluginVerifyException;

import javax.swing.*;
import java.io.File;

/**
 * Created by richie on 16/3/19.
 */
public class InstallFromDiskExecutor implements Executor {
    private String filePath;

    public InstallFromDiskExecutor(String filePath) {
        this.filePath = filePath;

    }

    @Override
    public String getTaskFinishMessage() {
        return "已成功安装";
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在解压文件" + filePath;
                    }

                    @Override
                    public void run(Process<java.lang.String> process) {

                    }
                },
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在安装";
                    }

                    @Override
                    public void run(Process<String> process) {
                        try {
                            PluginHelper.installPluginFromDisk(new File(filePath), new After() {
                                @Override
                                public void done() {
                                    FRLogger.getLogger().info("插件安装成功");
                                    PluginWebBridge.getHelper().showRestartMessage(Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                                }
                            });
                        } catch (PluginVerifyException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                        } catch (Exception e) {
                            FRLogger.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
