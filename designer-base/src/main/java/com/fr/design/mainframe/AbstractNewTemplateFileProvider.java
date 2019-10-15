package com.fr.design.mainframe;

import com.fr.stable.fun.mark.API;

/**
 * Created by kerry on 2019-10-14
 */
@API(level = NewTemplateFileProvider.CURRENT_LEVEL)
public abstract class AbstractNewTemplateFileProvider implements NewTemplateFileProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
