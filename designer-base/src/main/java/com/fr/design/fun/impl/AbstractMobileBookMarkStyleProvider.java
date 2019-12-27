package com.fr.design.fun.impl;

import com.fr.design.fun.MobileBookMarkStyleProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/24
 */
@API(level = MobileBookMarkStyleProvider.CURRENT_LEVEL)
public abstract class AbstractMobileBookMarkStyleProvider extends AbstractProvider implements MobileBookMarkStyleProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
