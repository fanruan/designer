package com.fr.design.fun.impl;

import com.fr.design.fun.MobileWidgetStyleProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

@API(level = MobileWidgetStyleProvider.CURRENT_LEVEL)
public abstract class AbstractMobileWidgetStyleProvider extends AbstractProvider implements MobileWidgetStyleProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

}
