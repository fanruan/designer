package com.fr.design.extra.exe.callback;

import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/26.
 */
public class UpdateOnlineCallback extends AbstractPluginTaskCallback {
    public PluginMarker toPluginMarker;


    public UpdateOnlineCallback(PluginMarker pluginMarker , PluginMarker toPluginMarker, JSCallback jsCallback) {
        this.pluginMarker = pluginMarker;
        this.toPluginMarker = toPluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress));
    }

    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            FRLogger.getLogger().info("更新成功");
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
        } else if (result.errorCode() == PluginErrorCode.OperationNotSupport.getCode()) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    Inter.getLocText(Inter.getLocText("FR-Designer-Plugin_Install_Success")),
                    Inter.getLocText("FR-Designer-Plugin_Install_Dependence"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null
            );
            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            PluginManager.getController().update(pluginMarker, toPluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, jsCallback));
        } else {
            FRLogger.getLogger().info("更新失败");
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
