package com.fr.start.module;


import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.messagecollect.StartupMessageCollector;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.module.Activator;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.record.analyzer.Metrics;
import com.fr.start.Designer;
import com.fr.start.ServerStarter;
import com.fr.start.SplashContext;
import com.fr.start.server.FineEmbedServer;
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

        startSub(DesignerWorkspaceProvider.class);
        registerEnvListener();
        //启动env
        startSub(EnvBasedModule.class);
        //designer模块启动好后，查看demo
        browserDemo();
        ExecutorService service = Executors.newSingleThreadExecutor(new NamedThreadFactory("FineEmbedServerStart"));
        service.submit(new Runnable() {
            @Override
            public void run() {
                FineEmbedServer.start();
            }
        });
        service.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    designer.show(args);
                } finally {
                    DesignerContext.getDesignerFrame().getProgressDialog().dispose();
                }
            }
        });
        service.shutdown();
        DesignerContext.getDesignerFrame().setVisible(true);
        //启动画面结束
        SplashContext.getInstance().hide();

        DesignerContext.getDesignerFrame().getProgressDialog().setVisible(true);
        startSub(StartFinishActivator.class);
        StartupMessageCollector.getInstance().recordStartupLog();
    }

    private void browserDemo() {

        if (getModule().leftFindSingleton(StartupArgs.class) != null && getModule().leftFindSingleton(StartupArgs.class).isDemo()) {
            ServerStarter.browserDemoURL();
        }
    }

    /**
     * 注册切换环境前后事件监听
     */
    private void registerEnvListener() {

        /*切换环境前，关闭所有相关模块，最后执行*/
        listenEvent(WorkspaceEvent.BeforeSwitch, new Listener<Workspace>(Integer.MIN_VALUE) {

            @Override
            public void on(Event event, Workspace current) {
                stopSub(EnvBasedModule.class);
            }
        });
        /*切换环境后，重新启动所有相关模块，最先执行*/
        listenEvent(WorkspaceEvent.AfterSwitch, new Listener<Workspace>(Integer.MAX_VALUE) {

            @Override
            public void on(Event event, Workspace current) {
                startSub(EnvBasedModule.class);
                // 切换后的环境是本地环境才启动内置服务器
                if (current.isLocal()) {
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            FineEmbedServer.start();
                        }
                    });
                    service.shutdown();
                }
            }
        });
        /*切换环境前，存储一下打开的所有文件对象，要先于 关闭相关模块部分 被触发*/
        listenEvent(WorkspaceEvent.BeforeSwitch, new Listener<Workspace>(Integer.MAX_VALUE) {
            @Override
            public void on(Event event, Workspace workspace) {
                HistoryTemplateListCache.getInstance().stash();
            }
        });

        /*切换环境后，装载一下打开的所有文件对象，优先级低于默认优先级，要后于 启动相关模块部分 被触发*/
        listenEvent(WorkspaceEvent.AfterSwitch, new Listener<Workspace>(Integer.MIN_VALUE) {
            @Override
            public void on(Event event, Workspace workspace) {
                HistoryTemplateListCache.getInstance().load();
            }
        });
    }


    @Override
    public void stop() {

    }
}
