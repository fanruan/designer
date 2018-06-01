package com.fr.design.extra.exe.callback;

import com.fr.design.extra.PluginUtils;
import com.fr.general.Inter;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/27.
 */
public class UninstallPluginCallback extends AbstractPluginTaskCallback {
    private JSCallback jsCallback;

    public UninstallPluginCallback(PluginMarker pluginMarker, JSCallback jsCallback){
        this.jsCallback = jsCallback;
        this.pluginMarker = pluginMarker;
    }

    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            jsCallback.execute("success");
            FineLoggerFactory.getLogger().info(Inter.getLocText("FR-Plugin_Delete_Success"));
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Plugin_Delete_Success"));
        }else if (result.errorCode() == PluginErrorCode.NeedUninstallDependingPluginFirst) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    Inter.getLocText(Inter.getLocText("FR-Plugin_Delete_Dependence")),
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
            PluginManager.getController().uninstall(pluginMarker, true, new UninstallPluginCallback(pluginMarker, jsCallback));
        } else {
            jsCallback.execute("failed");
            FineLoggerFactory.getLogger().info(Inter.getLocText("FR-Plugin_Delete_Failed"));
            JOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
