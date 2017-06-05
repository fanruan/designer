package com.fr.design.extra.exe.callback;

import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/27.
 */
public class UninstallPluginCallback implements PluginTaskCallback {
    private JSCallback jsCallback;

    public UninstallPluginCallback(JSCallback jsCallback){
        this.jsCallback = jsCallback;
    }

    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            jsCallback.execute("success");
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Delete_Success"));
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
        } else {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Delete_Failed"));
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
