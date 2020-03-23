package com.fr.design.fun.impl;

import com.fr.design.fun.ComponentLibraryPaneProcessor;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;
import com.fr.stable.fun.mark.Layer;

/**
 * created by Harrison on 2020/03/16
 **/
@API(level = ComponentLibraryPaneProcessor.CURRENT_LEVEL)
public abstract class AbstractComponentLibraryPaneProcessor extends AbstractProvider implements ComponentLibraryPaneProcessor {
    
    @Override
    public int currentAPILevel() {
        
        return ComponentLibraryPaneProcessor.CURRENT_LEVEL;
    }
    
    @Override
    public int layerIndex() {
        
        return Layer.DEFAULT_LAYER_INDEX;
    }
}
