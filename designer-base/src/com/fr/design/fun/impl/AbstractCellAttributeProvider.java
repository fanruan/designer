package com.fr.design.fun.impl;

import com.fr.design.fun.CellAttributeProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by zhouping on 2015/11/11.
 */
@API(level = CellAttributeProvider.CURRENT_LEVEL)
public abstract class AbstractCellAttributeProvider implements CellAttributeProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

}