package com.fr.design.extra.exe.callback;

import com.fr.general.Inter;
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
            String modifyMessage = isActive ? Inter.getLocText("FR-Designer-Plugin_Actived") : Inter.getLocText("FR-Designer-Plugin_Disabled");
            JOptionPane.showMessageDialog(null, modifyMessage);
        } else {
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }

}
