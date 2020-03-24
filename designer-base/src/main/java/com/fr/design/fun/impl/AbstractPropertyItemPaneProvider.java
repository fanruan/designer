package com.fr.design.fun.impl;

import com.fr.design.fun.PropertyItemPaneProvider;
import com.fr.design.mainframe.PaneHolder;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * created by Harrison on 2020/03/23
 **/
@API(level = PropertyItemPaneProvider.CURRENT_LEVEL)
public abstract class AbstractPropertyItemPaneProvider<T> extends AbstractProvider implements PropertyItemPaneProvider<T> {
    
    @Override
    public PaneHolder<T> getPaneHolder(Class<?> clazz) {
    
        if (sign().equals(clazz)) {
            return getPathHolder0();
        }
        return null;
    }
    
    protected abstract PaneHolder<T> getPathHolder0();
    
    protected abstract Class<T> sign();
    
    @Override
    public int currentAPILevel() {
        return PropertyItemPaneProvider.CURRENT_LEVEL;
    }
}
