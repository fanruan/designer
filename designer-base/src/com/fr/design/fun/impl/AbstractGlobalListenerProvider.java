package com.fr.design.fun.impl;

import com.fr.design.fun.GlobalListenerProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by zack on 2015/8/17.
 */
@API(level = GlobalListenerProvider.CURRENT_LEVEL)
public abstract class AbstractGlobalListenerProvider extends AbstractProvider implements GlobalListenerProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}