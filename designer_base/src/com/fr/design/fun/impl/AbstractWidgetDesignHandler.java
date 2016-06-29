package com.fr.design.fun.impl;

import com.fr.design.fun.WidgetDesignHandler;
import com.fr.stable.fun.mark.API;

/**
 * Coder: zack
 * Date: 2016/5/12
 * Time: 10:41
 */
@API(level = WidgetDesignHandler.CURRENT_LEVEL)
public abstract class AbstractWidgetDesignHandler implements WidgetDesignHandler {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}
