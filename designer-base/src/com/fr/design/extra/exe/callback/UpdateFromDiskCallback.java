package com.fr.design.extra.exe.callback;

import com.fr.design.extra.PluginOperateUtils;
import com.fr.design.extra.PluginUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTask;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Created by ibm on 2017/5/27.
 */
public class UpdateFromDiskCallback extends AbstractPluginTaskCallback {
    private File zipFile;
    private JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public UpdateFromDiskCallback(File zipFile, JSCallback jsCallback) {
        this.zipFile = zipFile;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT + "%"));
    }


    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            jsCallback.execute("success");
            FRLogger.getLogger().info(Inter.getLocText("FR-Plugin_Update_Success"));
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Plugin_Update_Success"));
        } else if (result.errorCode() == PluginErrorCode.NeedDealWithPluginDependency) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    Inter.getLocText(Inter.getLocText("FR-Plugin_Update_Dependence")),
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
            List<PluginTask> pluginTasks = result.getPreTasks();
            for(PluginTask pluginTask : pluginTasks){
                PluginMarker marker = pluginTask.getMarker();
                PluginOperateUtils.updatePluginOnline(marker, jsCallback);
            }
            PluginManager.getController().update(zipFile, new UpdateFromDiskCallback(zipFile, jsCallback));
        } else if(result.errorCode() == PluginErrorCode.NoPluginToUpdate){
            int rv = JOptionPane.showOptionDialog(
                    null,
                    Inter.getLocText("FR-Plugin_No_Plugin_Update"),
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
            PluginOperateUtils.installPluginFromDisk(zipFile, jsCallback);
        }else {
            jsCallback.execute("failed");
            FRLogger.getLogger().info(Inter.getLocText("FR-Plugin_Update_Failed"));
            JOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
