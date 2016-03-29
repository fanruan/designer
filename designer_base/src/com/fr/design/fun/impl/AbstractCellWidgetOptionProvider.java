package com.fr.design.fun.impl;

import com.fr.design.fun.CellWidgetOptionProvider;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractCellWidgetOptionProvider implements CellWidgetOptionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


}