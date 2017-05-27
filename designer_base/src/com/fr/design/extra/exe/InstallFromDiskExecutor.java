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
        return Inter.getLocText("FR-Designer-Plugin_Success_Install");
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return Inter.getLocText("FR-Designer-Plugin_Unzipping") + filePath;
                    }

                    @Override
                    public void run(Process<java.lang.String> process) {

                    }
                },
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return Inter.getLocText("FR-Designer-Plugin_Installing");
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
                                    FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Install_Success"));
                                } else if(result.errorCode() == PluginErrorCode.OperationNotSupport.getCode()){
                                    int rv = JOptionPane.showOptionDialog(
                                            null,
                                            Inter.getLocText(Inter.getLocText("FR-Designer-Plugin_Install_Dependence")),
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
                                    installWithDependence();
                                }else{
                                    FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Install_Failed"));
                                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    }
                }
        };
    }

    public void installWithDependence(){
        PluginManager.getController().install(new File(filePath), new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {
            }
            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Install_Success"));
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                } else{
                    FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Install_Failed"));
                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
