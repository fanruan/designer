package com.fr.design.fun.impl;

import com.fr.design.fun.ElementUIProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by richie on 16/4/25.
 */
@API(level = ElementUIProvider.CURRENT_LEVEL)
public abstract class AbstractElementUIProvider extends AbstractProvider implements ElementUIProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }
}
