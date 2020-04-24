package com.fr.design.fun.impl;


import com.fr.design.fun.FormAdaptiveConfigUIProcessor;
import com.fr.stable.fun.mark.API;

/**
 * Created by kerry on 2020-04-09
 */
@API(level = FormAdaptiveConfigUIProcessor.CURRENT_LEVEL)
public abstract class AbstractFormAdaptiveConfigUIProcessor  implements FormAdaptiveConfigUIProcessor {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

}
