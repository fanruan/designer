package com.fr.design.fun.impl;

import com.fr.design.fun.PluginInstallOptionProcessor;
import com.fr.stable.fun.mark.API;

/**
 * Created by Administrator on 2016/8/26.
 */
@API(level = PluginInstallOptionProcessor.CURRENT_LEVEL)
public abstract class AbstractPluginInstallOptionProcessor implements PluginInstallOptionProcessor{
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }


}
