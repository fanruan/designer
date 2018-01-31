package com.fr.start.module;

import com.fr.design.module.DesignerModule;
import com.fr.general.ModuleContext;
import com.fr.module.Activator;
import com.fr.stable.module.ModuleListener;

/**
 * Created by juhaoyu on 2018/1/31.
 * 触发原来的DesignerModule的启动
 * 之后慢慢将DesignerModule拆成Activator
 */
public class DesignerModuleActivator extends Activator {
    
    @Override
    public void start() {
        
        ModuleContext.registerModuleListener(getModule().findSingleton(ModuleListener.class));
        ModuleContext.startModule(DesignerModule.class.getName());
        ModuleContext.clearModuleListener();
    }
    
    @Override
    public void stop() {
    
    }
}
