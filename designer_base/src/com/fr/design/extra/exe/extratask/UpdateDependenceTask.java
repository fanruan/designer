package com.fr.design.extra.exe.extratask;

import com.fr.design.extra.exe.callback.JSCallback;
import com.fr.design.extra.exe.callback.UpdateDependenceCallback;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;

/**
 * Created by ibm on 2017/6/21.
 */
public class UpdateDependenceTask extends AbstractExtraPluginTask {

    public PluginMarker toPluginMarker;
    protected JSCallback jsCallback;

    public UpdateDependenceTask(PluginMarker pluginMarker, PluginMarker toPluginMarker, JSCallback jsCallback) {
        this.pluginMarker = pluginMarker;
        this.toPluginMarker = toPluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void doExtraPluginTask() {
        PluginManager.getController().update(pluginMarker, toPluginMarker, new UpdateDependenceCallback(pluginMarker, toPluginMarker, jsCallback));
    }
}
