package com.fr.start.module;


import com.fr.design.mainframe.DesignerContext;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.module.Activator;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.record.analyzer.Metrics;
import com.fr.start.Designer;
import com.fr.start.EnvSwitcher;
import com.fr.start.SplashContext;
import com.fr.startup.activators.BasicActivator;
import com.fr.workspace.Workspace;
import com.fr.workspace.WorkspaceEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by juhaoyu on 2018/1/8.
 */
@EnableMetrics
public class DesignerStartup extends Activator {

    @Override
    @Metrics
    public void start() {
        startSub(PreStartActivator.class);
        //启动基础部分
        startSub(BasicActivator.class);
        final String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        final Designer designer = new Designer(args);
        //启动env
        startSub(DesignerWorkspaceProvider.class);
        startSub(EnvBasedModule.class);
        getRoot().getSingleton(EnvSwitcher.class).switch2LastEnv();
        ExecutorService service = Executors.newSingleThreadExecutor();
        registerEnvListener();
        service.submit(new Runnable() {
            @Override
            public void run() {
                designer.show(args);
                DesignerContext.getDesignerFrame().getProgressDialog().dispose();
            }
        });
        service.shutdown();
        DesignerContext.getDesignerFrame().setVisible(true);
        //启动画面结束
        SplashContext.getInstance().hide();

        DesignerContext.getDesignerFrame().getProgressDialog().setVisible(true);
        startSub(StartFinishActivator.class);
    }

    /**
     * 切换环境时，重新启动所有相关模块
     */
    private void registerEnvListener() {

        listenEvent(WorkspaceEvent.BeforeSwitch, new Listener<Workspace>() {

            @Override
            public void on(Event event, Workspace param) {

                getSub(EnvBasedModule.class).stop();
            }
        });
        listenEvent(WorkspaceEvent.AfterSwitch, new Listener<Workspace>() {

            @Override
            public void on(Event event, Workspace param) {

                getSub(EnvBasedModule.class).start();
            }
        });
    }


    @Override
    public void stop() {

    }
}
