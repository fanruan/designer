package com.fr.design.fun.impl;

import com.fr.design.fun.TableDataDefineProvider;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractTableDataDefineProvider implements TableDataDefineProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
}