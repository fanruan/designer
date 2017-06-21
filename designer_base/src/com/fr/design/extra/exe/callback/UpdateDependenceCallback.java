package com.fr.design.extra.exe.callback;

import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/6/21.
 */
public class UpdateDependenceCallback  extends AbstractPluginTaskCallback {
    public PluginMarker toPluginMarker;
    protected JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public UpdateDependenceCallback(PluginMarker pluginMarker, PluginMarker toPluginMarker, JSCallback jsCallback){
        this.pluginMarker = pluginMarker;
        this.toPluginMarker = toPluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT + "%"));
    }


    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin-Update_Dependence_Success"));
        }else {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin-Update_Dependence_Failed"));
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}