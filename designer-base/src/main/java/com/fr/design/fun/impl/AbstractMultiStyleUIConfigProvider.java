package com.fr.design.fun.impl;

import com.fr.design.fun.CustomStyleUIConfigProvider;
import com.fr.design.fun.MultiStyleUIConfigProvider;
import com.fr.stable.fun.mark.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry on 2019-11-11
 */
@API(level = MultiStyleUIConfigProvider.CURRENT_LEVEL)
public abstract class AbstractMultiStyleUIConfigProvider implements MultiStyleUIConfigProvider {
    @Override
    public List<CustomStyleUIConfigProvider> getConfigList() {
        return new ArrayList<CustomStyleUIConfigProvider>();
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

}
