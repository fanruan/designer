package com.fr.design.fun.impl;

import com.fr.design.fun.TableDataSourceManagerProcessor;

/**
 * Coder: zack
 * Date: 2016/4/18
 * Time: 10:30
 */
public abstract class AbstractTDSourceManagerProcessor implements TableDataSourceManagerProcessor {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
}
