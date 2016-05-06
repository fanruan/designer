package com.fr.design.fun.impl;

import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.stable.fun.impl.AbstractProvider;


/**
 * Created by vito on 16/4/27.
 */
public abstract class AbstractWidgetAttrProvider extends AbstractProvider implements WidgetPropertyUIProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }
}
