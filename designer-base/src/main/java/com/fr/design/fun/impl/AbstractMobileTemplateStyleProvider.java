package com.fr.design.fun.impl;

import com.fr.design.fun.MobileTemplateStyleProvider;
import com.fr.design.fun.MobileWidgetStyleProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/31
 */
@API(level = MobileWidgetStyleProvider.CURRENT_LEVEL)
public abstract class AbstractMobileTemplateStyleProvider extends AbstractProvider implements MobileTemplateStyleProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
