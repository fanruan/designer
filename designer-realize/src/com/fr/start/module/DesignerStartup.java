package com.fr.start.module;

import com.fr.module.Activator;
import com.fr.stable.CoreActivator;
import com.fr.stable.module.ModuleListener;
import com.fr.start.Designer;
import com.fr.start.EnvSwitcher;
import com.fr.start.ReportSplashPane;
import com.fr.start.SplashWindow;
import com.fr.startup.activators.BasicActivator;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class DesignerStartup extends Activator {

    @Override
    public void start() {

        startSub(PreStartActivator.class);
        //启动基础部分
        startSub(BasicActivator.class);
        //启动画面
        SplashWindow splashWindow = createSplashWindow();
        String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        Designer designer = new Designer(args);
        //启动env
        startSub(DesignerEnvProvider.class);
        //启动各个模块
        getSub(CoreActivator.class).start();
        getSub("designer").start();
        getRoot().getSingleton(EnvSwitcher.class).switch2LastEnv();
        //启动设计器界面
        designer.show(args);
        //启动画面结束
        splashWindow.setVisible(false);
        splashWindow.dispose();
        startSub(StartFinishActivator.class);
    }

    private SplashWindow createSplashWindow() {

        ReportSplashPane reportSplashPane = new ReportSplashPane();
        SplashWindow splashWindow = new SplashWindow(reportSplashPane);
        getModule().setSingleton(ModuleListener.class, reportSplashPane.getModuleListener());
        return splashWindow;
    }

    @Override
    public void stop() {

    }
}
