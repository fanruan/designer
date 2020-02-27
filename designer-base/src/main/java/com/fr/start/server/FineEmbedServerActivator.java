package com.fr.start.server;

import com.fr.design.DesignerEnvManager;
import com.fr.log.FineLoggerFactory;
import com.fr.module.Activator;
import com.fr.module.ModuleRole;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.startup.FineWebApplicationInitializer;
import com.fr.third.springframework.web.SpringServletContainerInitializer;
import com.fr.third.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import com.fr.workspace.WorkContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;

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
            FineEmbedServerMonitor.getInstance().reset();
            //初始化tomcat
            initTomcat();
            tomcat.start();

        } catch (LifecycleException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            FineEmbedServerMonitor.getInstance().setComplete();
        }
    }

    @Override
    public synchronized void stop() {

        try {
            stopSpring();
            stopServerActivator();
            stopTomcat();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private void initTomcat() {

        tomcat = new Tomcat();

        tomcat.setPort(DesignerEnvManager.getEnvManager().getEmbedServerPort());
        // 设置解码uri使用的字符编码
        tomcat.getConnector().setURIEncoding(EncodeConstants.ENCODING_UTF_8);
        String docBase = new File(WorkContext.getCurrent().getPath()).getParent();

        //内置的上下文使用工程目录比如webroot
        String contextPath = "/" + ProductConstants.getAppFolderName();
        final Context context = tomcat.addContext(contextPath, docBase);
        context.setResources(new StandardRoot(context));
        Tomcat.initWebappDefaults(context);
        //覆盖tomcat的WebAppClassLoader
        context.setLoader(new FRTomcatLoader());


        //直接指定initializer，tomcat就不用再扫描一遍了
        SpringServletContainerInitializer initializer = new SpringServletContainerInitializer();
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(FineWebApplicationInitializer.class);
        context.addServletContainerInitializer(initializer, classes);
    }


    private void stopServerActivator() {

        ModuleRole.ServerRoot.stop();
    }

    private void stopSpring() {

        AnnotationConfigWebApplicationContext context = ModuleRole.ServerRoot.findSingleton(AnnotationConfigWebApplicationContext.class);
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
