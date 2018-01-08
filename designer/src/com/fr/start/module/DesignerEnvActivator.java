package com.fr.start.module;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.TemplatePane;
import com.fr.design.module.DesignerModule;
import com.fr.env.SignIn;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.module.Activator;
import com.fr.stable.module.ModuleListener;
import com.fr.start.StartServer;

/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerEnvActivator extends Activator {
    
    @Override
    public void start() {
        
        String[] args = getModule().findSingleton(StartupArgs.class).get();
        if (args != null) {
            for (String arg : args) {
                if (ComparatorUtils.equals(arg, "demo")) {
                    DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
                    StartServer.browserDemoURL();
                    break;
                }
            }
        }
        //设置好环境即可，具体跟环境有关的模块会自动调用
        switch2LastEnv();
        //启动core部分
        getSub("core").start();
        //启动其他模块
        //todo 也切换到新Module
        ModuleContext.registerModuleListener(getModule().findSingleton(ModuleListener.class));
        ModuleContext.startModule(DesignerModule.class.getName());
    }
    
    
    private void switch2LastEnv() {
        
        try {
            String current = DesignerEnvManager.getEnvManager().getCurEnvName();
            SignIn.signIn(DesignerEnvManager.getEnvManager().getEnv(current));
            if (!FRContext.getCurrentEnv().testServerConnectionWithOutShowMessagePane()) {
                throw new Exception(Inter.getLocText("Datasource-Connection_failed"));
            }
        } catch (Exception e) {
            TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
        }
    }
    
    
    @Override
    public void stop() {
        //清空模块
    }
}
