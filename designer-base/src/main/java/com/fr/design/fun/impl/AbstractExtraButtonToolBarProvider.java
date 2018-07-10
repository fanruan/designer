package com.fr.design.fun.impl;

import com.fr.design.fun.ExtraButtonToolBarProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by harry on 2016-12-23.
 */
@API(level = ExtraButtonToolBarProvider.CURRENT_LEVEL)
public abstract class AbstractExtraButtonToolBarProvider extends AbstractProvider implements ExtraButtonToolBarProvider {
    public int currentAPILevel() {
        return ExtraButtonToolBarProvider.CURRENT_LEVEL;
    }

    public String mark4Provider() {
        return getClass().getName();
    }
}
