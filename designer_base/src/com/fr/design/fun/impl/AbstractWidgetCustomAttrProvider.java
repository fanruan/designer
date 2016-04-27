package com.fr.design.fun.impl;

import com.fr.design.fun.WidgetCustomAttrProvider;
import com.fr.stable.fun.impl.AbstractProvider;


/**
 * Created by vito on 16/4/27.
 */
public abstract class AbstractWidgetCustomAttrProvider extends AbstractProvider implements WidgetCustomAttrProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }
}
