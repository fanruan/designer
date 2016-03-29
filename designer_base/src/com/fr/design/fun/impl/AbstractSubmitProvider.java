package com.fr.design.fun.impl;

import com.fr.design.fun.SubmitProvider;

public abstract class AbstractSubmitProvider implements SubmitProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


}