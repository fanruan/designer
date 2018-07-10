package com.fr.design.fun.impl;

import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
@API(level = FormWidgetOptionProvider.CURRENT_LEVEL)
public abstract class AbstractFormWidgetOptionProvider extends AbstractProvider implements FormWidgetOptionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public boolean isContainer() {
        return false;
    }
}