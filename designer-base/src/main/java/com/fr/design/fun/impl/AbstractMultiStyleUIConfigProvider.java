package com.fr.design.fun.impl;

import com.fr.design.fun.StyleUIConfigProvider;
import com.fr.design.fun.MultiStyleUIConfigProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry on 2019-11-11
 */
@API(level = MultiStyleUIConfigProvider.CURRENT_LEVEL)
public abstract class AbstractMultiStyleUIConfigProvider extends AbstractProvider implements MultiStyleUIConfigProvider {
    @Override
    public List<StyleUIConfigProvider> getConfigList() {
        return new ArrayList<StyleUIConfigProvider>();
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

}
