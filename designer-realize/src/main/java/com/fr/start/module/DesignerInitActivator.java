package com.fr.start.module;

import com.fr.module.Activator;
import com.fr.start.DesignerInitial;

/**
 * 设计器界面初始化
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019/9/25
 */
public class DesignerInitActivator extends Activator {


    @Override
    public void start() {
        DesignerInitial.init(findSingleton(StartupArgs.class).get());
    }

    @Override
    public void stop() {
        // void
    }
}
