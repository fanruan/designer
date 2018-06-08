package com.fr.start.module;

import com.fr.common.rpc.netty.RemoteCallClient;
import com.fr.design.mainframe.loghandler.socketio.DesignerSocketIO;
import com.fr.design.module.DesignerModule;
import com.fr.general.ModuleContext;
import com.fr.locale.InterMutableKey;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;

/**
 * Created by juhaoyu on 2018/1/31.
 * 触发原来的DesignerModule的启动
 * 之后慢慢将DesignerModule拆成Activator
 */
public class DesignerModuleActivator extends Activator implements Prepare {

    @Override
    public void start() {
        
        ModuleContext.startModule(DesignerModule.class.getName());
        DesignerSocketIO.init();
        RemoteCallClient.getInstance();
    }

    @Override
    public void stop() {
        ModuleContext.stopModule(DesignerModule.class.getName());
    }

    @Override
    public void prepare() {

        addMutable(InterMutableKey.Path, "com/fr/design/i18n/main", "com/fr/design/i18n/chart");
    }
}
