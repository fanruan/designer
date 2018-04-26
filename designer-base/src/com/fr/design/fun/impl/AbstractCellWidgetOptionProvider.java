package com.fr.design.fun.impl;

import com.fr.design.fun.CellWidgetOptionProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
@API(level = CellWidgetOptionProvider.CURRENT_LEVEL)
public abstract class AbstractCellWidgetOptionProvider extends AbstractProvider implements CellWidgetOptionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}