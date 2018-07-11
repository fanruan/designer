package com.fr.design.fun.impl;

import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
@API(level = ParameterWidgetOptionProvider.CURRENT_LEVEL)
public abstract class AbstractParameterWidgetOptionProvider extends AbstractProvider implements ParameterWidgetOptionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}