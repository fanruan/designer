package com.fr.design.fun.impl;

import com.fr.design.fun.ToolbarItemProvider;

/**
 * Created by richie on 15/12/1.
 */
public abstract class AbstractToolbarItem implements ToolbarItemProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

}