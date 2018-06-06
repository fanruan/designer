package com.fr.start.server;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.event.EventDispatcher;
import com.fr.log.FineLoggerFactory;
import com.fr.module.ModuleRole;
import com.fr.stable.lifecycle.AbstractLifecycle;
import com.fr.startup.FineWebApplicationInitializer;
import com.fr.third.springframework.web.SpringServletContainerInitializer;
import com.fr.third.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.VirtualWebappLoader;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by juhaoyu on 2018/6/5.
 */
public class FineEmbedServer extends AbstractLifecycle {
    
    private static final FineEmbedServer INSTANCE = new FineEmbedServer();
    
    private Tomcat tomcat;
    
    public static FineEmbedServer getInstance() {
        
        return INSTANCE;
    }
    
    private FineEmbedServer() {}
    
    @Override
    protected synchronized void executeStart() {
        
        EventDispatcher.fire(EmbedServerEvent.BeforeStart);
        try {
            //初始化tomcat
            initTomcat();
            tomcat.start();
        } catch (LifecycleException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        EventDispatcher.fire(EmbedServerEvent.AfterStart);
    }
    
    private void initTomcat() {
        
        tomcat = new Tomcat();
        
        tomcat.setPort(DesignerEnvManager.getEnvManager().getEmbedServerPort());
        String docBase = new File(FRContext.getCurrentEnv().getPath()).getParent();
        String appName = "/" + FRContext.getCurrentEnv().getAppName();
        Context context = tomcat.addContext(appName, docBase);
        tomcat.addServlet(appName, "default", "org.apache.catalina.servlets.DefaultServlet");
        //覆盖tomcat的WebAppClassLoader
        context.setLoader(new FRTomcatLoader());
        
        //直接指定initializer，tomcat就不用再扫描一遍了
        SpringServletContainerInitializer initializer = new SpringServletContainerInitializer();
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(FineWebApplicationInitializer.class);
        context.addServletContainerInitializer(initializer, classes);
    }
    
    @Override
    protected synchronized void executeStop() {
        
        EventDispatcher.fire(EmbedServerEvent.BeforeStop);
        try {
            stopSpring();
            stopServerActivator();
            stopTomcat();
        } catch (LifecycleException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        EventDispatcher.fire(EmbedServerEvent.AfterStop);
    }
    
    private void stopServerActivator() {

        ModuleRole.ServerRoot.stop();
    }
    
    private void stopSpring() {
        
        AnnotationConfigWebApplicationContext context = ModuleRole.ServerRoot.getSingleton(AnnotationConfigWebApplicationContext.class);
        if (context != null) {
            context.stop();
            context.destroy();
        }
    }
    
    private void stopTomcat() throws LifecycleException {
        
        tomcat.stop();
        tomcat.destroy();
    }
    
    
    /**
     * Created by juhaoyu on 2018/6/5.
     * 自定义的tomcat loader，主要用于防止内置服务器再加载一遍class
     */
    private static class FRTomcatLoader extends VirtualWebappLoader {
        
        @Override
        public ClassLoader getClassLoader() {
            
            return this.getClass().getClassLoader();
        }
        
    }
    
}
