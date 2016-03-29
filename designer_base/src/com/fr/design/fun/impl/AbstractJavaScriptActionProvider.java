package com.fr.design.fun.impl;

import com.fr.design.fun.JavaScriptActionProvider;

/**
 * Created by zack on 2015/8/20.
 */
public abstract class AbstractJavaScriptActionProvider implements JavaScriptActionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

}