package com.fr.design.extra.exe.callback;

import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.PluginOperateUtils;

import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.control.AbstractDealPreTaskCallback;
import com.fr.plugin.manage.control.PluginTask;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;


/**
 * Created by ibm on 2017/5/26.
 */
public class InstallOnlineCallback extends AbstractDealPreTaskCallback {
    protected JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public InstallOnlineCallback(PluginTask pluginTask, JSCallback jsCallback){
        super(pluginTask);
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT + "%"));
    }


    @Override
    protected void allDone(PluginTaskResult result) {
        String pluginInfo = PluginOperateUtils.getSuccessInfo(result);
        if (result.isSuccess()) {
            String switchedInfo = PluginOperateUtils.getSwitchedInfo(result);
            jsCallback.execute("success");
            FineLoggerFactory.getLogger().info(pluginInfo + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Success") + switchedInfo);
            FineJOptionPane.showMessageDialog(null, pluginInfo + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Success") + switchedInfo);
        } else if(result.errorCode() == PluginErrorCode.HasLowerPluginWhenInstall){
            int rv = JOptionPane.showOptionDialog(
                    null,
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Has_Install_Lower"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null
            );
            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            PluginMarker pluginMarker = result.getCurrentTask().getMarker();
            PluginOperateUtils.updatePluginOnline(pluginMarker, jsCallback);
        }else {
            jsCallback.execute("failed");
            FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Failed"));
            FineJOptionPane.showMessageDialog(null, pluginInfo, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }



}
