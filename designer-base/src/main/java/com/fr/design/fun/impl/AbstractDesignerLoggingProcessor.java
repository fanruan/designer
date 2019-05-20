package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerLoggingProcessor;
import com.fr.stable.fun.mark.API;

@API(level = DesignerLoggingProcessor.CURRENT_LEVEL)
public abstract class AbstractDesignerLoggingProcessor implements DesignerLoggingProcessor {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}