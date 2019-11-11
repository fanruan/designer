package com.fr.design.fun.impl;

import com.fr.base.Style;
import com.fr.design.fun.CustomStyleUIConfigProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.mark.API;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * Created by kerry on 2019-11-11
 */
@API(level = CustomStyleUIConfigProvider.CURRENT_LEVEL)
public class AbstractCustomStyleUIConfigProvider implements CustomStyleUIConfigProvider {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public String configName() {
        return StringUtils.EMPTY;
    }

    @Override
    public JComponent uiComponent(ChangeListener changeListener) {
        return null;
    }

    @Override
    public Style updateConfig() {
        return null;
    }

    @Override
    public void populateConfig(Style style) {

    }
}
