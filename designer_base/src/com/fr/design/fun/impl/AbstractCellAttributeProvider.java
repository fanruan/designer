package com.fr.design.fun.impl;

import com.fr.design.fun.CellAttributeProvider;

/**
 * Created by zhouping on 2015/11/11.
 */
public abstract class AbstractCellAttributeProvider implements CellAttributeProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

}