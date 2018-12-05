package com.fr.design.fun.impl;

import com.fr.design.fun.ParameterExpandablePaneUIProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

@API(level = ParameterExpandablePaneUIProvider.CURRENT_LEVEL)
public abstract class AbstractParameterExpandablePaneUIProvider extends AbstractProvider implements ParameterExpandablePaneUIProvider{

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
