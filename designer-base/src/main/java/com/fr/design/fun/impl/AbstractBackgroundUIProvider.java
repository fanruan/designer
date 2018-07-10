package com.fr.design.fun.impl;

import com.fr.design.fun.BackgroundUIProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by richie on 16/5/18.
 */
@API(level = BackgroundUIProvider.CURRENT_LEVEL)
public abstract class AbstractBackgroundUIProvider extends AbstractProvider implements BackgroundUIProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
