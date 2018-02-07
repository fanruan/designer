package com.fr.start;

import java.io.File;
import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

public class TomcatFRHost {
    private static Tomcat tomcat;

    public static Tomcat getTomcat() {
        return tomcat;
    }

    private static StandardServer server;

    private static AprLifecycleListener listener;

    public static void main(String[] args) throws Exception {
        tomcat = new Tomcat();
        // 主机名，或ip
//        tomcat.setHostname("localhost");
        // 设置端口，80为默认端口
        tomcat.setPort(8071);
        // tomcat用于存储自身的信息，可以随意指定，最好包含在项目目录下
        tomcat.setBaseDir(".");
        // 建立server参照tomcat文件结构
        server = (StandardServer) tomcat.getServer();
        listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
        // 将appBase设为本项目所在目录
        //tomcat.getHost().setAppBase(".");
        tomcat.getHost().setAppBase(
                System.getProperty("user.dir") + File.separator + ".");

        // 第二个参数对应docBase为web应用路径，目录下应有WEB-INF,WEB-INF下要有web.xml
        // 启动tomcat
        try {
            tomcat.start();
            Context ct1 = tomcat.addWebapp("/WebReport", "/Users/momeak/Documents/Working/develop/others/tomcatsrc/WebReport");
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
//        Context ct1 = tomcat.addWebapp("/examples", "/Users/momeak/Documents/Working/develop/others/tomcatsrc/examples");

//        Context ct = tomcat.addWebapp("", "/Users/momeak/Documents/Working/develop/others/tomcatsrc/webapps/ROOT");

//        tomcat.getServer().await();
        System.out.println("启动成功");
    }
}
