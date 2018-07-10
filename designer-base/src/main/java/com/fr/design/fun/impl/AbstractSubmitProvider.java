package com.fr.design.fun.impl;

import com.fr.design.fun.SubmitProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

@API(level = SubmitProvider.CURRENT_LEVEL)
public abstract class AbstractSubmitProvider extends AbstractProvider implements SubmitProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}