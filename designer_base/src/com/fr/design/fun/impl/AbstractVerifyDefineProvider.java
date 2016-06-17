package com.fr.design.fun.impl;

import com.fr.design.fun.VerifyDefineProvider;
import com.fr.stable.fun.impl.AbstractProvider;

/**
 * Created by richie on 16/6/8.
 */
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
