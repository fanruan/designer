package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerStartOpenFileProcessor;
import com.fr.stable.fun.mark.API;

import static com.fr.stable.fun.mark.Layer.DEFAULT_LAYER_INDEX;

/**
 * Created by rinoux on 2016/12/16.
 */
@API(level = DesignerStartOpenFileProcessor.CURRENT_LEVEL)
public class AbstractDesignerStartOpenFileProcessor {
    public int currentAPILevel() {
        return DesignerStartOpenFileProcessor.CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}
