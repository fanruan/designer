package com.fr.start.server;

import com.fr.module.ModuleContext;
import java.awt.SystemTray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fr.general.GeneralContext;
import com.fr.stable.ProductConstants;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.start.StartServer;

public class TomcatHost {

    private static FRTomcat tomcat;
    private StandardServer server;
    private AprLifecycleListener listener;

//    private Server server;
    private MultiOutputStream multiOutputStream = null;
    private File outLogFile = null;
    private int currentPort = -1;
    // 内置服务器一个端口下面可以有多个应用,但是content不能重名
    private Map<String, Context> webAppsMap = new HashMap<String, Context>();
    private List<TomcatServerListener> listenerList = new ArrayList<TomcatServerListener>();
    private boolean isDemoAppLoaded = false;

    public TomcatHost(int port) {
        this.currentPort = port;
        initServer();

        initLogFileAndOutputStream();

        // TODO: 将HostTomcatServer放到ServerTray中去
        tryStartServerTray();
    }

    public static FRTomcat getTomcat() {
        return tomcat;
    }

    private void initServer() {
        try {
            //直接用自定义的，不用server.xml
            this.tomcat = new FRTomcat();
            this.tomcat.setPort(this.currentPort);
            this.tomcat.setBaseDir(StableUtils.getInstallHome());
            this.server = (StandardServer) tomcat.getServer();
            this.listener = new AprLifecycleListener();
            this.server.addLifecycleListener(listener);
            this.tomcat.getHost().setAppBase(StableUtils.getInstallHome() + File.separator + ".");
            this.tomcat.getConnector().setURIEncoding("UTF-8");
        } catch (Exception e) {
            //todo 最好加一个用server.xml
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    private void initLogFileAndOutputStream() {
        // log文件放置的位置
        File logDir = null;
        String installHome = StableUtils.getInstallHome();
        if (installHome == null) {// 没有installHome的时候，就放到user.home下面喽
            logDir = new File(ProductConstants.getEnvHome() + File.separator + ProjectConstants.LOGS_NAME);
        } else {
            // james：logs放在安装目录下面
            logDir = new File(installHome + File.separator + ProjectConstants.LOGS_NAME + File.separator + "tomcat");
        }
        StableUtils.mkdirs(logDir);
        DateFormat fateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar curCalendar = Calendar.getInstance();
        outLogFile = new File(logDir, "tomcat_" + fateFormat.format(curCalendar.getTime()) + ".log");

        try {
            multiOutputStream = new MultiOutputStream();
            multiOutputStream.addOutputStream(new FileOutputStream(outLogFile, true));
            multiOutputStream.addOutputStream(System.out);
            System.setErr(new PrintStream(multiOutputStream));
            System.setOut(new PrintStream(multiOutputStream));
        } catch (IOException ioe) {
            FRContext.getLogger().error(ioe.getMessage(), ioe);
        }
    }

    private synchronized void addWebApplication(String context, String webappsPath) {
        FRContext.getLogger().info("The new  Application Path is: \n" + webappsPath + ", it will be added.");
        if (webAppsMap.get(context) != null) {
            Context webapp = webAppsMap.remove(context);
        }
        try {
            if (!isStarted()) {
                start();
            }
            Context webapp = tomcat.addWebapp(context, webappsPath);
            webAppsMap.put(context, webapp);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    private void addAndStartWebApplication(String context, String webAppPath) {
        addWebApplication(context, webAppPath);
    }

    /**
     * Get MultiOutputStream.
     */
    public MultiOutputStream getMultiOutputStream() {
        return this.multiOutputStream;
    }

    /**
     * Get out log file
     */
    public File getOutLogFile() {
        return this.outLogFile;
    }

    private Server getServer() {
        if (server == null) {
            initServer();
        }

        return server;
    }

    /**
     * Start
     *
     * @throws Exception
     */
    public void start() throws Exception {
        tomcat.start();
        for (int i = 0; i < listenerList.size(); i++) {
            TomcatServerListener listener = TomcatHost.this.getLinstener(i);
            listener.started(this);
        }
    }

    /**
     * Stop
     *
     * @throws Exception
     */
    public void stop() throws Exception {

        tomcat.stop();

        for (int i = 0; i < listenerList.size(); i++) {
            TomcatServerListener listener = this.getLinstener(i);
            listener.stopped(this);
        }

        StartServer.currentEnvChanged();
        server = null;//重置server
    }

    /**
     * Is started
     *
     * @throws Exception
     */
    public boolean isStarted() throws Exception {
        return getServer().getState().isAvailable();
    }

    public void addListener(TomcatServerListener listener) {
        this.listenerList.add(listener);
    }

    public int getLinstenerCount() {
        return this.listenerList.size();
    }

    public TomcatServerListener getLinstener(int index) {
        if (index < 0 || index >= this.getLinstenerCount()) {
            return null;
        }

        return this.listenerList.get(index);
    }

    public void clearLinsteners() {
        this.listenerList.clear();
    }

    /**
     * 尝试启动系统托盘
     */
    private void tryStartServerTray() {
        if (SystemTray.isSupported()) {
            new ServerTray(this);
        } else {
            FRContext.getLogger().error("Do not support the SystemTray!");
        }
    }

    public void exit() {
        try {
            getServer().stop();
        } catch (LifecycleException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

        for (int i = 0; i < listenerList.size(); i++) {
            TomcatServerListener listener = this.getLinstener(i);
            listener.exited(this);
        }

        try {
            getServer().destroy();
        } catch (LifecycleException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        StartServer.currentEnvChanged();

    }

    public int getCurrentPort() {
        return currentPort;
    }

    /**
     * 安装目录下的默认的WebReport，这个只执行一次,除了预览demo，其他的不要调用这个方法
     */
    public void addAndStartInstallHomeWebApp() {
        if (!isDemoAppLoaded) {
            String installHome = StableUtils.getInstallHome();
            String webApplication = StableUtils.pathJoin(new String[]{installHome, ProjectConstants.WEBAPP_NAME});

            if (new File(webApplication).isDirectory()) {
                addAndStartWebApplication("/" + ProjectConstants.WEBAPP_NAME, webApplication);
            }
        }
        isDemoAppLoaded = true;
    }

    /**
     * 加载Env下的报表运行环境
     */
    public void addAndStartLocalEnvHomeWebApp() {
        String name = DesignerEnvManager.getEnvManager().getCurEnvName();
        if (name.equals(Inter.getLocText("Default"))) {
            isDemoAppLoaded = true;
        }
        Env env = FRContext.getCurrentEnv();
        if (env instanceof LocalEnv) {
            String webApplication = new File(env.getPath()).getParent();
            FRContext.getLogger().info(Inter.getLocText("INFO-Reset_Webapp") + ":" + webApplication);
            addAndStartWebApplication("/" + GeneralContext.getCurrentAppNameOfEnv(), webApplication);
        }
    }

    public boolean isDemoAppLoaded() {
        return isDemoAppLoaded;
    }

}