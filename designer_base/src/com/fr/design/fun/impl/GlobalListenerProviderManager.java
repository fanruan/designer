package com.fr.design.fun.impl;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.GlobalListenerProvider;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginRuntime;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.plugin.observer.PluginEventType;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by juhaoyu on 2017/6/15.
 * 管理正在运行中插件的GlobalListenerProvider接口
 */
public class GlobalListenerProviderManager {
    
    private static final GlobalListenerProviderManager INSTANCE = new GlobalListenerProviderManager();
    
    private Map<GlobalListenerProvider, AWTEventListener> map = new HashMap<>();
    
    private GlobalListenerProviderManager() {}
    
    public static GlobalListenerProviderManager getInstance() {
        
        return INSTANCE;
    }
    
    public void init() {
        
        Set<GlobalListenerProvider> providers = ExtraDesignClassManager.getInstance().getArray(GlobalListenerProvider.XML_TAG);
        addAWTEventListeners(providers);
        listenPlugin();
    }
    
    private void listenPlugin() {
        
        PluginFilter filter = new PluginFilter() {
            
            @Override
            public boolean accept(PluginContext context) {
                
                return context.contain(PluginModule.ExtraDesign, GlobalListenerProvider.XML_TAG);
            }
        };
        PluginEventListener onRun = new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                PluginRuntime runtime = event.getContext().getRuntime();
                Set<GlobalListenerProvider> providers = runtime.get(PluginModule.ExtraDesign, GlobalListenerProvider.XML_TAG);
                addAWTEventListeners(providers);
            }
        };
        PluginEventListener onStop = new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                PluginRuntime runtime = event.getContext().getRuntime();
                Set<GlobalListenerProvider> providers = runtime.get(PluginModule.ExtraDesign, GlobalListenerProvider.XML_TAG);
                removeAWTEventListeners(providers);
            }
        };
        GeneralContext.listenPlugin(PluginEventType.AfterRun, onRun, filter);
        GeneralContext.listenPlugin(PluginEventType.BeforeStop, onStop, filter);
        
        
    }
    
    private void addAWTEventListeners(Set<GlobalListenerProvider> providers) {
        
        AWTEventListener listener;
        for (GlobalListenerProvider provider : providers) {
            listener = provider.listener();
            Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
            add(provider, listener);
        }
    }
    
    private void removeAWTEventListeners(Set<GlobalListenerProvider> providers) {
        
        AWTEventListener listener;
        for (GlobalListenerProvider provider : providers) {
            listener = pop(provider);
            Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
        }
    }
    
    private synchronized void add(GlobalListenerProvider provider, AWTEventListener listener) {
        
        map.put(provider, listener);
    }
    
    private synchronized AWTEventListener pop(GlobalListenerProvider provider) {
        
        return map.remove(provider);
    }
 
}
