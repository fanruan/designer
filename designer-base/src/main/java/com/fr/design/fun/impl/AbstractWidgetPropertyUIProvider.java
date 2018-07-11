package com.fr.design.fun.impl;

import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;


/**
 * Created by vito on 16/4/27.
 */
@API(level = WidgetPropertyUIProvider.CURRENT_LEVEL)
public abstract class AbstractWidgetPropertyUIProvider extends AbstractProvider implements WidgetPropertyUIProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }
}
