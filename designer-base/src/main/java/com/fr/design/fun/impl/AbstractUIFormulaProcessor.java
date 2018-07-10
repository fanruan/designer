package com.fr.design.fun.impl;

import com.fr.design.fun.UIFormulaProcessor;
import com.fr.stable.fun.mark.API;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
@API(level = UIFormulaProcessor.CURRENT_LEVEL)
public abstract class AbstractUIFormulaProcessor implements UIFormulaProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

}