package com.fr.design.fun.impl;

import com.fr.design.fun.MessageObjectProvider;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

@API(level = MessageObjectProvider.CURRENT_LEVEL)
public abstract class AbstractMessageObjectProvider extends AbstractProvider implements MessageObjectProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public String mark4Provider() {
        return getClass().getName();
    }
}
