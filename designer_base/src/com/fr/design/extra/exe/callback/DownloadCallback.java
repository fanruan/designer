package com.fr.design.extra.exe.callback;

import com.fr.design.extra.exe.extratask.ExtraPluginTask;
import com.fr.general.Inter;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/26.
 */
public class DownloadCallback extends AbstractPluginTaskCallback {
    private ExtraPluginTask extraPluginTask;

    public DownloadCallback(ExtraPluginTask extraPluginTask, JSCallback jsCallback) {
        this.extraPluginTask = extraPluginTask;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress));
    }

    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            extraPluginTask.doExtraPluginTask();
        } else {
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
