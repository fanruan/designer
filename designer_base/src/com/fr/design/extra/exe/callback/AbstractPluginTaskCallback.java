package com.fr.design.extra.exe.callback;

import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.control.ProgressCallback;

import java.io.File;

/**
 * Created by ibm on 2017/5/26.
 */
public abstract class AbstractPluginTaskCallback implements ProgressCallback{

    protected PluginMarker pluginMarker;
    protected JSCallback jsCallback;

    @Override
    public void updateProgress(String description, double aProgress) {
    }


}
