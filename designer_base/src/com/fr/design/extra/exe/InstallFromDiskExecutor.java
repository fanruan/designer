package com.fr.design.extra.exe;

import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;

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
                        PluginManager.getController().install(new File(filePath), new ProgressCallback() {
                            @Override
                            public void updateProgress(String description, double progress) {
                            }
                            @Override
                            public void done(PluginTaskResult result) {
                                if (result.isSuccess()) {
                                    FRLogger.getLogger().info("插件安装成功");
                                } else if(result.errorCode() == PluginErrorCode.OperationNotSupport.getCode()){
                                    int rv = JOptionPane.showOptionDialog(
                                            null,
                                            Inter.getLocText("安装依赖"),
                                            Inter.getLocText("FR-Designer-Plugin_Warning"),
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            null,
                                            null
                                    );
                                    if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                                        return;
                                    }
                                    installWithDepenndence();
                                }else{
                                    FRLogger.getLogger().info("插件安装失败");
                                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    }
                }
        };
    }

    public void installWithDepenndence(){
        PluginManager.getController().install(new File(filePath), new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {
            }
            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    FRLogger.getLogger().info("插件安装成功");
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                } else{
                    FRLogger.getLogger().info("插件安装失败");
                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
