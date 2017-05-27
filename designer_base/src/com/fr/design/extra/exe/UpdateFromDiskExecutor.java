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
public class UpdateFromDiskExecutor implements Executor {

    private String filePath;

    public UpdateFromDiskExecutor(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getTaskFinishMessage() {
        return Inter.getLocText("FR-Designer-Plugin_Update_End");
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
                        PluginManager.getController().update(new File(filePath), new ProgressCallback() {
                            @Override
                            public void updateProgress(String description, double progress) {

                            }

                            @Override
                            public void done(PluginTaskResult result) {
                                if (result.isSuccess()) {
                                    FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Update_Success"));
                                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                                } else if (result.errorCode() == PluginErrorCode.OperationNotSupport.getCode()) {
                                    updatePluginWithDependence();
                                } else {
                                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    }
                }
        };
    }

    public void updatePluginWithDependence() {
        PluginManager.getController().update(new File(filePath), new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {

            }

            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                } else {
                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
