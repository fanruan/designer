package com.fr.design.fun.impl;

import com.fr.design.fun.ParameterWidgetOptionProvider;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractParameterWidgetOptionProvider implements ParameterWidgetOptionProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

}