package com.fr.design.extra.exe.callback;

import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.PluginUtils;

import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/27.
 */
public class ModifyStatusCallback implements PluginTaskCallback{
    private boolean isActive;
    private JSCallback jsCallback;

    public ModifyStatusCallback (boolean isActive, JSCallback jsCallback){
        this.isActive = isActive;
        this.jsCallback = jsCallback;
    }
    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            jsCallback.execute("success");
            String modifyMessage = isActive ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Has_Been_Disabled_Duplicate") : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Plugin_Has_Been_Actived_Duplicate");
            FineJOptionPane.showMessageDialog(null, modifyMessage);
        } else {
            FineJOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }

}
