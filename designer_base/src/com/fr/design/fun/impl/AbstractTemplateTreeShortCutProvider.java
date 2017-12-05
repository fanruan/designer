package com.fr.design.fun.impl;

import com.fr.design.actions.UpdateAction;
import com.fr.design.fun.TemplateTreeShortCutProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by hzzz on 2017/11/30.
 */
@API(level = TemplateTreeShortCutProvider.CURRENT_LEVEL)
public abstract class AbstractTemplateTreeShortCutProvider extends UpdateAction implements TemplateTreeShortCutProvider {

    @Override
    public int currentAPILevel() {
        return TemplateTreeShortCutProvider.CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
