package com.fr.design.report.fit.menupane;


/**
 * Created by kerry on 2020-04-09
 */
public abstract class AbstractFormAdaptiveConfigUI implements FormAdaptiveConfigUI {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}
