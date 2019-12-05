package com.fr.design.extra.exe.callback;

import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.PluginOperateUtils;
import com.fr.design.extra.PluginUtils;

import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTask;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Created by ibm on 2017/5/26.
 */
public class InstallFromDiskCallback extends AbstractPluginTaskCallback {
    private File zipFile;
    private JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public InstallFromDiskCallback(final File zipFile, final JSCallback jsCallback) {
        this.zipFile = zipFile;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT +  "%"));
    }


    @Override
    public void done(PluginTaskResult result) {
        String pluginInfo = PluginOperateUtils.getSuccessInfo(result);
        if (result.isSuccess()) {
            String switchedInfo = PluginOperateUtils.getSwitchedInfo(result);
            jsCallback.execute("success");
            FineLoggerFactory.getLogger().info(pluginInfo + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Success") + switchedInfo);
            FineJOptionPane.showMessageDialog(null, pluginInfo + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Success") + switchedInfo);
        } else if (result.errorCode() == PluginErrorCode.NeedDealWithPluginDependency) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Dependence"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    FineJOptionPane.OPTION_YES_NO_CANCEL,
                    null
            );
            if (rv == JOptionPane.NO_OPTION || rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            List<PluginTask> pluginTasks = result.getPreTasks();
            for(PluginTask pluginTask : pluginTasks){
                PluginMarker marker = pluginTask.getMarker();
                PluginOperateUtils.installPluginOnline(marker, jsCallback);
            }
            PluginManager.getController().install(zipFile, new InstallFromDiskCallback(zipFile, jsCallback));
        } else if(result.errorCode() == PluginErrorCode.HasLowerPluginWhenInstall){
            int rv = JOptionPane.showOptionDialog(
                    null,
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Has_Install_Lower"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    FineJOptionPane.OPTION_YES_NO_CANCEL,
                    null
            );
            if (rv == JOptionPane.NO_OPTION || rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            PluginOperateUtils.updatePluginFromDisk(zipFile, jsCallback);
        }else {
            jsCallback.execute("failed");
            FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Failed"));
            FineJOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Failed"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
