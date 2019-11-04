package com.fr.design.style.preference.impl;

import com.fr.design.style.preference.PreferenceConfigProvider;
import com.fr.design.style.preference.PreferenceTabConfig;
import com.fr.stable.fun.mark.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry on 2019-11-04
 */
@API(level = PreferenceConfigProvider.CURRENT_LEVEL)
public abstract class AbstractPreferenceConfigProvider implements PreferenceConfigProvider {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public List<PreferenceTabConfig> getConfigList() {
        return new ArrayList<PreferenceTabConfig>();
    }
}
