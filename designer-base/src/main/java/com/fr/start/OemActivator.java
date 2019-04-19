package com.fr.start;

import com.fr.design.fun.OemProcessor;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;

/**
 * 设计器OEM注册
 */
public class OemActivator extends Activator implements Prepare {

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    @Override
    public void prepare() {
        // 加入Oem
        setSingleton(OemProcessor.class, new OemImpl());
    }

}
