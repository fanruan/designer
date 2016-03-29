package com.fr.design.fun.impl;

import com.fr.design.fun.GlobalListenerProvider;

/**
 * Created by zack on 2015/8/17.
 */
public abstract class AbstractGlobalListenerProvider implements GlobalListenerProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


}