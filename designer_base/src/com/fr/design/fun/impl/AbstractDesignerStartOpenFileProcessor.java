package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerStartOpenFileProcessor;
import com.fr.stable.fun.mark.API;

/**
 * Created by rinoux on 2016/12/16.
 */
@API(level = DesignerStartOpenFileProcessor.CURRENT_LEVEL)
public abstract class AbstractDesignerStartOpenFileProcessor implements DesignerStartOpenFileProcessor {
    public int currentAPILevel() {
        return DesignerStartOpenFileProcessor.CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}
