package com.fr.design.style.preference.impl;

import com.fr.base.Style;
import com.fr.design.style.preference.PreferenceTabConfig;
import com.fr.stable.StringUtils;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * Created by kerry on 2019-11-04
 */
public abstract class AbstractPreferenceTabConfig implements PreferenceTabConfig {
    @Override
    public String tabName() {
        return StringUtils.EMPTY;
    }

    @Override
    public JComponent tabComponent(ChangeListener changeListener) {
        return null;
    }

    @Override
    public Style updateTabConfig() {
        return null;
    }

    @Override
    public void populateTabConfig(Style style) {

    }
}
