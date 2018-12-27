package com.fr.design.fun.impl;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by zack on 2016/1/18.
 */
@API(level = HyperlinkProvider.CURRENT_LEVEL)
public abstract class AbstractHyperlinkProvider extends AbstractProvider implements HyperlinkProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public NameableCreator createHyperlinkCreator() {
        return new NameObjectCreator(text(), target(), appearance());
    }

    @Override
    public String text() {
        return StringUtils.EMPTY;
    }

    @Override
    public Class target() {
        return null;
    }

    @Override
    public Class<? extends BasicBeanPane> appearance() {
        return null;
    }
}