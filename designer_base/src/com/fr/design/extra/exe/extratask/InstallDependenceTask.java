package com.fr.design.extra.exe.extratask;

import com.fr.design.extra.exe.callback.InstallDependenceCallback;
import com.fr.design.extra.exe.callback.JSCallback;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;

/**
 * Created by ibm on 2017/6/21.
 */
public class InstallDependenceTask extends AbstractExtraPluginTask {
    protected JSCallback jsCallback;


    public InstallDependenceTask(PluginMarker pluginMarker, JSCallback jsCallback) {
        this.pluginMarker = pluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void doExtraPluginTask() {
        PluginManager.getController().install(pluginMarker, new InstallDependenceCallback(pluginMarker, jsCallback));
    }
}