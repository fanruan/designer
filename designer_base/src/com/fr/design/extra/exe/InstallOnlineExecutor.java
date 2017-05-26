package com.fr.design.extra.exe;

import com.fr.design.extra.LoginCheckContext;
import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.bbs.BBSPluginLogin;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;

import javax.swing.*;

/**
 * Created by richie on 16/3/19.
 */
public class InstallOnlineExecutor implements Executor {

    private String pluginInfo;

    public InstallOnlineExecutor(String pluginInfo) {
        this.pluginInfo = pluginInfo;
    }

    @Override
    public String getTaskFinishMessage() {
        return "task succeed";
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在下载插件:" + pluginInfo.split("_")[0];
                    }

                    @Override
                    public void run(final Process<String> process) {
                        //下载插件
                        if (!BBSPluginLogin.getInstance().hasLogin()) {
                            LoginCheckContext.fireLoginCheckListener();
                        }
                        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
                        if (BBSPluginLogin.getInstance().hasLogin()) {
                            PluginManager.getController().download(pluginMarker, new ProgressCallback() {
                                @Override
                                public void updateProgress(String description, double aProgress) {
                                    process.process(Math.round(aProgress * 100) + "%");
                                }

                                @Override
                                public void done(PluginTaskResult result) {
                                    if (result.isSuccess()) {
                                        installPlugin(pluginMarker);
                                    } else {
                                        JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });

                        }
                    }
                }
        };
    }

    public void installPlugin(PluginMarker pluginMarker) {
        PluginManager.getController().install(pluginMarker, new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {

            }

            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    FRLogger.getLogger().info("插件安装成功");
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                } else if (result.errorCode() == PluginErrorCode.OperationNotSupport.getCode()) {
                    int rv = JOptionPane.showOptionDialog(
                            null,
                            Inter.getLocText("安装依赖"),
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
                    installPluginWithDependence(pluginMarker);
                } else {
                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void installPluginWithDependence(PluginMarker pluginMarker) {
        PluginManager.getController().install(pluginMarker, new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {
            }

            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    FRLogger.getLogger().info("插件安装成功");
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                } else {
                    FRLogger.getLogger().info("插件安装失败");
                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
