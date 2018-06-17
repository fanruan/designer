package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.general.ComparatorUtils;
import com.fr.module.Activator;
import com.fr.start.EnvSwitcher;
import com.fr.start.ServerStarter;

/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerWorkspaceProvider extends Activator {

    @Override
    public void start() {

        String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        if (args != null) {
            for (String arg : args) {
                if (ComparatorUtils.equals(arg, "demo")) {
                    DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
                    ServerStarter.browserDemoURL();
                    break;
                }
            }
        }
        getRoot().setSingleton(EnvSwitcher.class, new EnvSwitcher());
        //设置好环境即可，具体跟环境有关的模块会自动调用
        getRoot().getSingleton(EnvSwitcher.class).switch2LastEnv();
    }


    @Override
    public void stop() {
        //清空模块
        getRoot().removeSingleton(EnvSwitcher.class);
    }


}
