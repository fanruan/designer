package com.fr.design.fun.impl;

import com.fr.design.fun.HyperlinkProvider;

/**
 * Created by zack on 2016/1/18.
 */
public abstract class AbstractHyperlinkProvider implements HyperlinkProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

}