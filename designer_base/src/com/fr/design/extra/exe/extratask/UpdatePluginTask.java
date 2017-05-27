package com.fr.design.extra.exe.extratask;

import com.fr.design.extra.exe.callback.JSCallback;
import com.fr.design.extra.exe.callback.UpdateOnlineCallback;
import com.fr.form.ui.WaterMark;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;

/**
 * Created by ibm on 2017/5/27.
 */
public class UpdatePluginTask implements ExtraPluginTask {

    public PluginMarker pluginMarker;
    public PluginMarker toPluginMarker;
    public JSCallback jsCallback;

    public UpdatePluginTask(PluginMarker pluginMarker, PluginMarker toPluginMarker, JSCallback jsCallback){
        this.pluginMarker = pluginMarker;
        this.toPluginMarker = toPluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void doExtraPluginTask() {
        PluginManager.getController().update(pluginMarker, toPluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, jsCallback));
    }
}
