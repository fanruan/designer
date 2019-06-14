package com.fr.start.module;


import com.fr.design.file.HistoryTemplateListCache;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.module.Activator;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.record.analyzer.Metrics;
import com.fr.runtime.FineRuntime;
import com.fr.start.DesignerInitial;
import com.fr.start.ServerStarter;
import com.fr.start.server.FineEmbedServer;
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
        getSub("parallel").start();
        //designer模块启动好后，查看demo
        browserDemo();
        
        startSub(DesignerShowActivator.class);
        startSub(StartFinishActivator.class);
        FineRuntime.startFinish();
    }
    
    private void browserDemo() {
        
        if (getModule().leftFindSingleton(StartupArgs.class) != null && getModule().leftFindSingleton(StartupArgs.class).isDemo()) {
            ServerStarter.browserDemoURL();
        }
    }
    
    
    @Override
    public void stop() {
    
    }
}
