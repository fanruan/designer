package com.fr.design.extra.tradition.callback;

import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.PluginStatusCheckCompletePane;
import com.fr.design.extra.PluginUtils;

import com.fr.log.FineLoggerFactory;
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
            FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_Update_Success"));
            FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Successful"));
        } else if (result.errorCode() == PluginErrorCode.OperationNotSupport) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_Install_Dependence"),
                    com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_Install_Success"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    FineJOptionPane.OPTION_YES_NO_CANCEL,
                    null
            );
            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            PluginManager.getController().update(pluginMarker, toPluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, pane));
        } else {
            FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_Delete_Failed"));
            FineJOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
