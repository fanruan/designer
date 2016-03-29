package com.fr.design.fun.impl;

import com.fr.design.fun.PresentKindProvider;

/**
 * @author richie
 * @date 2015-05-22
 * @since 8.0
 */
public abstract class AbstractPresentKindProvider implements PresentKindProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


}