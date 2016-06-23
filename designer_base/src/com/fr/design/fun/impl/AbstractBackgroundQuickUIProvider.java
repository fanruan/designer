package com.fr.design.fun.impl;

import com.fr.design.fun.BackgroundQuickUIProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by richie on 16/5/18.
 */
@API(level = BackgroundQuickUIProvider.CURRENT_LEVEL)
public abstract class AbstractBackgroundQuickUIProvider extends AbstractProvider implements BackgroundQuickUIProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
