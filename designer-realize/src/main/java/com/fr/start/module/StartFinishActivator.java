package com.fr.start.module;

import com.fr.design.fun.impl.GlobalListenerProviderManager;
import com.fr.module.Activator;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class StartFinishActivator extends Activator {

    @Override
    public void start() {
        GlobalListenerProviderManager.getInstance().init();
    }

    @Override
    public void stop() {
    }
}
