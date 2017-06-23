package com.fr.design.extra.tradition.callback;

import com.fr.design.extra.PluginStatusCheckCompletePane;
import com.fr.design.extra.PluginUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/31.
 */
public class UpdateOnlineCallback implements ProgressCallback {
    private PluginStatusCheckCompletePane pane;
    private PluginMarker pluginMarker;
    private PluginMarker toPluginMarker;

    public UpdateOnlineCallback(PluginMarker pluginMarker, PluginMarker toPluginMarker, PluginStatusCheckCompletePane pane){
        this.pluginMarker = pluginMarker;
        this.toPluginMarker = toPluginMarker;
        this.pane = pane;
    }
    public void updateProgress(String description, double progress){
        pane.setProgress(progress);
    }

    public void done(PluginTaskResult result){
        if (result.isSuccess()) {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Update_Success"));
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
        } else if (result.errorCode() == PluginErrorCode.OperationNotSupport) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    Inter.getLocText(Inter.getLocText("FR-Designer-Plugin_Install_Dependence")),
                    Inter.getLocText("FR-Designer-Plugin_Install_Success"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null
            );
            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            PluginManager.getController().update(pluginMarker, toPluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, pane));
        } else {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Delete_Failed"));
            JOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
