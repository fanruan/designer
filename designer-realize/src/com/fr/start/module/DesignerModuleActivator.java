package com.fr.start.module;

import com.fr.design.mainframe.loghandler.socketio.DesignerSocketIO;
import com.fr.design.module.DesignerModule;
import com.fr.general.ModuleContext;
import com.fr.locale.InterMutableKey;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;
import com.fr.stable.module.ModuleListener;

/**
 * Created by juhaoyu on 2018/1/31.
 * 触发原来的DesignerModule的启动
 * 之后慢慢将DesignerModule拆成Activator
 */
public class DesignerModuleActivator extends Activator implements Prepare {
    
    @Override
    public void start() {
        
        ModuleContext.registerModuleListener(getModule().upFindSingleton(ModuleListener.class));
        ModuleContext.startModule(DesignerModule.class.getName());
        ModuleContext.clearModuleListener();

        DesignerSocketIO.init();
    }
    
    @Override
    public void stop() {
    
    }
    
    @Override
    public void prepare() {
        
        addMutable(InterMutableKey.Path, "com/fr/design/i18n/main", "com/fr/design/i18n/chart");
    }
}
