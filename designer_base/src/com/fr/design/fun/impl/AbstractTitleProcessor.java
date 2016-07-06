package com.fr.design.fun.impl;

import com.fr.design.fun.TitlePlaceProcessor;
import com.fr.stable.fun.mark.API;

@API(level = TitlePlaceProcessor.CURRENT_LEVEL)
public abstract class AbstractTitleProcessor implements TitlePlaceProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }


}