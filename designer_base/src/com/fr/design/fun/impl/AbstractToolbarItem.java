package com.fr.design.fun.impl;

import com.fr.design.fun.ToolbarItemProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by richie on 15/12/1.
 */
@API(level = ToolbarItemProvider.CURRENT_LEVEL)
public abstract class AbstractToolbarItem extends AbstractProvider implements ToolbarItemProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}