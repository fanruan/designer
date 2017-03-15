package com.fr.design.fun.impl;

import com.fr.data.impl.DBTableData;
import com.fr.design.actions.UpdateAction;
import com.fr.design.fun.DBTableDataMenuHandler;
import com.fr.stable.fun.mark.API;

/**
 * Created by xiaxiang on 2017/1/15.
 */
@API(level = DBTableDataMenuHandler.CURRENT_LEVEL)
public abstract class AbstractDBTableDataMenuHandler implements DBTableDataMenuHandler {
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

    public UpdateAction createQueryAction() {
        return null;
    }

    @Override
    public DBTableData update() {
        return null;
    }

    @Override
    public void populate(DBTableData dbTableData) {

    }
}
