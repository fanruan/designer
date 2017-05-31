package com.fr.design.extra.exe.extratask;

import com.fr.design.extra.exe.callback.JSCallback;
import com.fr.design.extra.exe.callback.UpdateOnlineCallback;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;

/**
 * Created by ibm on 2017/5/27.
 */
public class UpdatePluginTask extends AbstractExtraPluginTask {

    public PluginMarker toPluginMarker;
    protected JSCallback jsCallback;

    public UpdatePluginTask(PluginMarker pluginMarker, PluginMarker toPluginMarker, JSCallback jsCallback) {
        this.pluginMarker = pluginMarker;
        this.toPluginMarker = toPluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void doExtraPluginTask() {
        PluginManager.getController().update(pluginMarker, toPluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, jsCallback));
    }
}
