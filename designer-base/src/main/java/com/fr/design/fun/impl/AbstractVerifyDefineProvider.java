package com.fr.design.fun.impl;

import com.fr.design.fun.VerifyDefineProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by richie on 16/6/8.
 */
@API(level = VerifyDefineProvider.CURRENT_LEVEL)
public abstract class AbstractVerifyDefineProvider extends AbstractProvider implements VerifyDefineProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
