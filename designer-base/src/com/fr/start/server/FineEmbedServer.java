package com.fr.start.server;

import com.fr.event.EventDispatcher;
import com.fr.module.ModuleContext;

/**
 * Created by juhaoyu on 2018/6/6.
 */
public abstract class FineEmbedServer {
    
    public synchronized static void start() {
        
        EventDispatcher.fire(EmbedServerEvent.BeforeStart);
        ModuleContext.getModule(FineEmbedServerActivator.class).start();
        EventDispatcher.fire(EmbedServerEvent.AfterStart);
    }
    
    public synchronized static void stop() {
        
        EventDispatcher.fire(EmbedServerEvent.BeforeStop);
        ModuleContext.getModule(FineEmbedServerActivator.class).stop();
        EventDispatcher.fire(EmbedServerEvent.AfterStop);
    }
    
    public static boolean isRunning() {
        
        return ModuleContext.getModule(FineEmbedServerActivator.class).isRunning();
    }
}
