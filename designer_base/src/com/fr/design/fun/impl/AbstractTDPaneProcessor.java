package com.fr.design.fun.impl;

import com.fr.design.fun.TableDataPaneProcessor;
import com.fr.stable.fun.mark.API;

/**
 * Coder: zack
 * Date: 2016/4/18
 * Time: 10:30
 */
@API(level = TableDataPaneProcessor.CURRENT_LEVEL)
public abstract class AbstractTDPaneProcessor implements TableDataPaneProcessor {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}
