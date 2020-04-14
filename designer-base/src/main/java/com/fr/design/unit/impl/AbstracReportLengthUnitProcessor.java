package com.fr.design.unit.impl;

import com.fr.design.unit.ReportLengthUnitProcessor;

/**
 * Created by kerry on 2020-04-09
 */
public abstract class AbstracReportLengthUnitProcessor implements ReportLengthUnitProcessor {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }
}
