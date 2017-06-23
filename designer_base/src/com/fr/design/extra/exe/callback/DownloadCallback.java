package com.fr.design.extra.exe.callback;

import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.exe.extratask.ExtraPluginTask;
import com.fr.general.Inter;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;

/**
 * Created by ibm on 2017/5/26.
 */
public class DownloadCallback extends AbstractPluginTaskCallback {
    private ExtraPluginTask extraPluginTask;
    private JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public DownloadCallback(final ExtraPluginTask extraPluginTask, final JSCallback jsCallback) {
        this.extraPluginTask = extraPluginTask;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT +  "%"));
    }

    @Override
    public void done(PluginTaskResult result) {
        if (result.isSuccess()) {
            extraPluginTask.doExtraPluginTask();
        } else {
            jsCallback.execute("failed");
            JOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
