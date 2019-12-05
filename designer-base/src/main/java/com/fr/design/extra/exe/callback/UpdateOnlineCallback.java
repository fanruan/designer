package com.fr.design.extra.exe.callback;

import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.PluginOperateUtils;

import com.fr.log.FineLoggerFactory;
import com.fr.plugin.manage.control.AbstractDealPreTaskCallback;
import com.fr.plugin.manage.control.PluginTask;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/26.
 */
public class UpdateOnlineCallback extends AbstractDealPreTaskCallback {
    protected JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public UpdateOnlineCallback(PluginTask pluginTask, JSCallback jsCallback) {
        super(pluginTask);
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT + "%"));
    }

    @Override
    public void allDone(PluginTaskResult result) {
        String pluginInfo = PluginOperateUtils.getSuccessInfo(result);
        if (result.isSuccess()) {
            jsCallback.execute("success");
            FineLoggerFactory.getLogger().info(pluginInfo + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Update_Success"));
            FineJOptionPane.showMessageDialog(null,pluginInfo + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Update_Success"));
        } else {
            jsCallback.execute("failed");
            FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Update_Failed"));
            FineJOptionPane.showMessageDialog(null, pluginInfo, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
