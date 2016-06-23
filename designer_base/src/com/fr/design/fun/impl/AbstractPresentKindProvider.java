package com.fr.design.fun.impl;

import com.fr.design.fun.PresentKindProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author richie
 * @date 2015-05-22
 * @since 8.0
 */
@API(level = PresentKindProvider.CURRENT_LEVEL)
public abstract class AbstractPresentKindProvider extends AbstractProvider implements PresentKindProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}