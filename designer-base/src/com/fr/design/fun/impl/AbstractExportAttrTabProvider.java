package com.fr.design.fun.impl;

import com.fr.design.fun.ExportAttrTabProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by vito on 16/5/5.
 */
@API(level = ExportAttrTabProvider.CURRENT_LEVEL)
public abstract class AbstractExportAttrTabProvider extends AbstractProvider implements ExportAttrTabProvider  {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }
}
