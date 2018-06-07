package com.fr.start.module;

import com.fr.core.env.EnvConfig;
import com.fr.core.env.EnvEvent;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.module.Activator;
import com.fr.start.Designer;
import com.fr.start.EnvSwitcher;
import com.fr.start.SplashContext;
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
        String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        Designer designer = new Designer(args);
        //启动env
        startSub(DesignerEnvProvider.class);
        startSub(EnvBasedModule.class);
        getRoot().getSingleton(EnvSwitcher.class).switch2LastEnv();
        registerEnvListener();
        //启动设计器界面
        designer.show(args);
        //启动画面结束
        SplashContext.getInstance().hide();
        startSub(StartFinishActivator.class);
    }
    
    /**
     * 切换环境时，重新启动所有相关模块
     */
    private void registerEnvListener() {
        
        listenEvent(EnvEvent.BEFORE_SIGN_OUT, new Listener<EnvConfig>() {
            
            @Override
            public void on(Event event, EnvConfig param) {
                
                getSub(EnvBasedModule.class).stop();
            }
        });
        listenEvent(EnvEvent.AFTER_SIGN_IN, new Listener<EnvConfig>() {
            
            @Override
            public void on(Event event, EnvConfig param) {
                
                getSub(EnvBasedModule.class).start();
            }
        });
    }
    
    
    @Override
    public void stop() {

    }
}
