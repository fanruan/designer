package com.fr.start.module;


import com.fr.module.Activator;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.record.analyzer.Metrics;
import com.fr.runtime.FineRuntime;
import com.fr.start.ServerStarter;

/**
 * Created by juhaoyu on 2018/1/8.
 */
@EnableMetrics
public class DesignerStartup extends Activator {

    @Override
    @Metrics
    public void start() {
        startSub(PreStartActivator.class);
        startSub("parallel");
        //designer模块启动好后，查看demo
        browserDemo();
        startSub(StartFinishActivator.class);
        FineRuntime.startFinish();
    }

    private void browserDemo() {

        if (findSingleton(StartupArgs.class) != null && findSingleton(StartupArgs.class).isDemo()) {
            ServerStarter.browserDemoURL();
        }
    }


    @Override
    public void stop() {

    }
}
