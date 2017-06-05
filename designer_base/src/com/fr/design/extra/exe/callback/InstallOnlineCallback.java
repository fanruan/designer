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
public class InstallOnlineCallback extends AbstractPluginTaskCallback {
    protected JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public InstallOnlineCallback(PluginMarker pluginMarker, JSCallback jsCallback){
        this.pluginMarker = pluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT + "%"));
    }


    @Override
    public void done(PluginTaskResult result) {
        jsCallback.execute("success");
        if (result.isSuccess()) {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Install_Success"));
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
            //执行JS回调
            PluginManager.getController().install(pluginMarker, new InstallOnlineCallback(pluginMarker, jsCallback));
        } else {
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
