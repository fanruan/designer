package com.fr.start.server;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.log.FineLoggerFactory;
import com.fr.module.Activator;
import com.fr.module.ModuleRole;
import com.fr.stable.EncodeConstants;
import com.fr.startup.FineWebApplicationInitializer;
import com.fr.third.springframework.web.SpringServletContainerInitializer;
import com.fr.third.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import com.fr.workspace.WorkContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;

import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by juhaoyu on 2018/6/5.
 */
public class FineEmbedServerActivator extends Activator {
    
    private Tomcat tomcat;
    
    @Override
    public synchronized void start() {
        
        try {
            //初始化tomcat
            initTomcat();
            tomcat.start();
        } catch (LifecycleException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
    
    @Override
    public synchronized void stop() {
        
        try {
            stopSpring();
            stopServerActivator();
            stopTomcat();
        } catch (LifecycleException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
    
    private void initTomcat() {
        
        tomcat = new Tomcat();
        
        tomcat.setPort(DesignerEnvManager.getEnvManager().getEmbedServerPort());
        // 设置解码uri使用的字符编码
        tomcat.getConnector().setURIEncoding(EncodeConstants.ENCODING_UTF_8);
        String docBase = new File(WorkContext.getCurrent().getPath()).getParent();
        String appName = "/" + FRContext.getCommonOperator().getAppName();
        Context context = tomcat.addContext(appName, docBase);

        //添加过滤器
        addFilter(context);

        addDefaultServlet(context);
        //覆盖tomcat的WebAppClassLoader
        context.setLoader(new FRTomcatLoader());
        
        //直接指定initializer，tomcat就不用再扫描一遍了
        SpringServletContainerInitializer initializer = new SpringServletContainerInitializer();
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(FineWebApplicationInitializer.class);
        context.addServletContainerInitializer(initializer, classes);
    }

    private void addFilter(Context context){
            FilterDef filterDef = new FilterDef();
            filterDef.setFilterName("Forbidden");
            filterDef.setFilterClass("com.fr.start.server.ForbidType");

            FilterMap filterMap = new FilterMap();
            filterMap.setFilterName("Forbidden");
            //指定过滤文件类型
            filterMap.addURLPattern("*.txt");
            filterMap.addURLPattern("*.xml");
            filterMap.addURLPattern("*.jar");

            context.addFilterDef(filterDef);
            context.addFilterMap(filterMap);
    }
    
    private void addDefaultServlet(Context context) {
        
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMapping("/","default");
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
    private static class FRTomcatLoader extends WebappLoader {
        
        @Override
        public ClassLoader getClassLoader() {
            
            return this.getClass().getClassLoader();
        }
        
    }
    
}
