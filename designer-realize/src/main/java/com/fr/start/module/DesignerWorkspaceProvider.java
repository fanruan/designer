package com.fr.start.module;

import com.fr.design.DesignerEnvManager;

import com.fr.module.Activator;
import com.fr.start.EnvSwitcher;

/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerWorkspaceProvider extends Activator {

    @Override
    public void start() {
        //检查环境
        DesignerEnvManager.checkNameEnvMap();

        EnvSwitcher switcher = new EnvSwitcher();
        //设置好环境即可，具体跟环境有关的模块会自动调用
        switcher.switch2LastEnv();
        getRoot().setSingleton(EnvSwitcher.class, switcher);
    }


    @Override
    public void stop() {
        //清空模块
        getRoot().removeSingleton(EnvSwitcher.class);
    }


}
