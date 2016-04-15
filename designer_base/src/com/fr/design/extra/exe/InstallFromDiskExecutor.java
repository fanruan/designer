package com.fr.design.extra.exe;

import com.fr.design.RestartHelper;
import com.fr.design.extra.After;
import com.fr.design.extra.PluginHelper;
import com.fr.design.extra.PluginWebBridge;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import org.apache.poi.poifs.crypt.AgileDecryptor;

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
                    public void run() {
                    }
                },
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在安装";
                    }

                    @Override
                    public void run() {
                        try {
                            PluginHelper.installPluginFromDisk(new File(filePath), new After() {
                                @Override
                                public void done() {
                                    FRLogger.getLogger().info("插件安装成功");
                                    PluginWebBridge.getHelper().showRestartMessage(Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                                }
                            });
                        } catch (Exception e1) {
                            FRLogger.getLogger().error(e1.getMessage());
                        }
                    }
                }
        };
    }
}
