package com.fr.design.extra.exe;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.extra.*;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.bbs.BBSPluginLogin;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by richie on 16/3/19.
 */
public class UpdateOnlineExecutor implements Executor {

    private String[] pluginInfos;
    private static final int PERCENT_100 = 100;

    public UpdateOnlineExecutor(String[] pluginInfos) {
        this.pluginInfos = pluginInfos;
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
                        return null;
                    }

                    @Override
                    public void run(Process<String> process) {
                        if (!(BBSPluginLogin.getInstance().hasLogin())){
                            LoginCheckContext.fireLoginCheckListener();
                        }
                        if (BBSPluginLogin.getInstance().hasLogin()) {
                            List<PluginMarker> pluginMarkerList = new ArrayList<PluginMarker>();
                            for (int i = 0; i < pluginInfos.length; i++) {
                                pluginMarkerList.add(PluginUtils.createPluginMarker(pluginInfos[i]));
                            }
                            updatePlugins(pluginMarkerList, process);                        }
                    }
                }
        };
    }

    public void updatePluginWithDependence(PluginMarker pluginMarker, PluginMarker toMarker) {
        PluginManager.getController().update(pluginMarker, toMarker, new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {

            }

            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                } else {
                    JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void updatePlugins(List<PluginMarker> pluginMarkerList, Process<String> process) {
        for (int i = 0; i < pluginMarkerList.size(); i++) {
            try {
                int a = i;
                //todo check下此插件的最新版本
                String latestPluginInfo = PluginUtils.getLatestPluginInfo(pluginMarkerList.get(i).getPluginID());
                if (StringUtils.isEmpty(latestPluginInfo) || PluginConstants.CONNECTION_404.equals(latestPluginInfo)) {
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Connect_Failed"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JSONObject resultArr = new JSONObject(latestPluginInfo);
                String latestPluginVersion = (String) resultArr.get("version");
                PluginManager.getController().update(pluginMarkerList.get(i), PluginMarker.create(pluginMarkerList.get(i).getPluginID(), latestPluginVersion), new ProgressCallback() {
                    @Override
                    public void updateProgress(String description, double progress) {
                        process.process(PERCENT_100 / pluginMarkerList.size() * (a + 1) + "%");

                    }

                    @Override
                    public void done(PluginTaskResult result) {
                        if (result.isSuccess()) {
                            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                        } else if (result.errorCode() == PluginErrorCode.OperationNotSupport.getCode()) {
                            updatePluginWithDependence(pluginMarkerList.get(a), PluginMarker.create(pluginMarkerList.get(a).getPluginID(), latestPluginVersion));
                        } else {
                            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

}
