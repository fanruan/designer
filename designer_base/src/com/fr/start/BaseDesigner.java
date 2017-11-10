/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.start;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.RestartHelper;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.fun.DesignerStartOpenFileProcessor;
import com.fr.design.fun.GlobalListenerProvider;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.TemplatePane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.module.DesignModule;
import com.fr.design.utils.DesignUtils;
import com.fr.env.SignIn;
import com.fr.file.FILE;
import com.fr.file.FILEFactory;
import com.fr.file.FileFILE;
import com.fr.general.*;
import com.fr.plugin.PluginCollector;
import com.fr.plugin.manage.PluginManager;
import com.fr.stable.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * The main class of Report Designer.
 */
public abstract class BaseDesigner extends ToolBarMenuDock {

    private static final int LOAD_TREE_MAXNUM = 10;

    private static final int MESSAGEPORT = 51462;

    private Timer timer;

    public BaseDesigner(String[] args) {
        if (isDebug()) {
            setDebugEnv();
        }
        RestartHelper.deleteRecordFilesWhenStart();

        DesignUtils.setPort(getStartPort());
        // 如果端口被占用了 说明程序已经运行了一次,也就是说，已经建立一个监听服务器，现在只要给服务器发送命令就好了
        if (DesignUtils.isStarted()) {
            DesignUtils.clientSend(args);
            return;
        }
        BuildContext.setBuildFilePath(buildPropertiesPath());

        //下面这两句的位置不能随便调换，因为会影响语言切换的问题
        initLanguage();

        // 先加载设计器的国际化文件
        Inter.loadLocaleFile(GeneralContext.getLocale(), DesignModule.LOCALE_FILE_PATH);


        SplashWindow splashWindow = new SplashWindow(createSplashPane());
        if (args != null) {
            for (String arg : args) {
                if (ComparatorUtils.equals(arg, "demo")) {
                    DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
                    StartServer.browserDemoURL();
                    break;
                }
            }
        }

        // 初始化look and feel.这个在预加载之前执行是因为lookAndFeel里的东西，预加载时也要用到
        DesignUtils.initLookAndFeel();

        DesignUtils.creatListeningServer(getStartPort(), startFileSuffix());
        //初始化插件引擎
        PluginManager.init();
        // 初始化Log Handler
        DesignerEnvManager.loadLogSetting();
        DesignerFrame df = createDesignerFrame();

        // 默认加载工作目录，用于读取License
        switch2LastEnv();

        initDefaultFont();
        // 必须先初始化Env再去startModule, 不然会导致lic读取不到
        ModuleContext.startModule(module2Start());

        // 再次加载工作目录，用于读取工作目录下的各种插件
        switch2LastEnv();

        ModuleContext.clearModuleListener();
        collectUserInformation();
        showDesignerFrame(args, df, false);
        for (int i = 0; !TemplateTreePane.getInstance().getTemplateFileTree().isTemplateShowing() && i < LOAD_TREE_MAXNUM; i++) {
            TemplateTreePane.getInstance().getTemplateFileTree().refresh();
        }

        splashWindow.setVisible(false);
        splashWindow.dispose();

        bindGlobalListener();

        showErrorPluginsMessage();
    }

    private void bindGlobalListener() {
        Set<GlobalListenerProvider> providers = ExtraDesignClassManager.getInstance().getArray(GlobalListenerProvider.XML_TAG);
        for (GlobalListenerProvider provider : providers) {
            Toolkit.getDefaultToolkit().addAWTEventListener(provider.listener(), AWTEvent.KEY_EVENT_MASK);
        }
    }

    private void showErrorPluginsMessage() {
        if (timer == null) {
            timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] plugins = PluginCollector.getCollector().getErrorPlugins();
                    if (ArrayUtils.isNotEmpty(plugins)) {
                        String text = StableUtils.join(plugins, ",") + ": " + Inter.getLocText("FR-Designer_Plugin_Should_Update_Please_Contact_Developer");
                        int r = JOptionPane.showConfirmDialog(null, text, Inter.getLocText("FR-Designer_Plugin_Should_Update_Title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (r == JOptionPane.OK_OPTION) {
                            WebViewDlgHelper.createPluginDialog();
                        }
                    }
                    timer.stop();
                }
            });
            timer.start();
        }
    }

    protected String[] startFileSuffix() {
        return new String[]{".cpt", ".xls", ".xlsx", ".frm", ".form", ".cht", ".chart"};
    }

    protected DesignerFrame createDesignerFrame() {
        return new DesignerFrame(this);
    }

    protected int getStartPort() {
        return MESSAGEPORT;
    }

    protected void initLanguage() {
        //这两句的位置不能随便调换，因为会影响语言切换的问题
        FRContext.setLanguage(DesignerEnvManager.getEnvManager(false).getLanguage());
        DesignerEnvManager.checkNameEnvMap();
    }

    protected void initDefaultFont() {

    }

    /**
     * build得路径
     *
     * @return build得路径
     */
    public String buildPropertiesPath() {
        return "/com/fr/stable/build.properties";
    }


    protected SplashPane createSplashPane() {
        return new SplashPane();
    }

    //在VM options里加入-Ddebug=true激活
    private boolean isDebug() {
        return "true".equals(System.getProperty("debug"));
    }

    private static final int DEBUG_PORT = 51463;

    //端口改一下，环境配置文件改一下。便于启动两个设计器，进行对比调试
    private void setDebugEnv() {
        DesignUtils.setPort(DEBUG_PORT);
        DesignerEnvManager.setEnvFile(new File(ProductConstants.getEnvHome() + File.separator + ProductConstants.APP_NAME + "Env_debug.xml"));
    }

    private void switch2LastEnv() {
        try {
            String current = DesignerEnvManager.getEnvManager().getCurEnvName();
            SignIn.signIn(DesignerEnvManager.getEnvManager().getEnv(current));
            if (!FRContext.getCurrentEnv().testServerConnectionWithOutShowMessagePane()) {
                throw new Exception(Inter.getLocText("Datasource-Connection_failed"));
            }
        } catch (Exception e) {
            TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
        }
    }

    private void showDesignerFrame(String[] args, final DesignerFrame df,
                                   boolean isException) {
        try {
            FILE file = null;
            if (args != null && args.length > 0) {
                // p:需要打开这个报表文件,这个代码不能删除.
                for (String arg : args) {
                    if (ComparatorUtils.equals("demo", arg)) {
                        file = FILEFactory.createFILE(FILEFactory.ENV_PREFIX + DesignerEnvManager.getEnvManager().getLastOpenFile());
                        break;
                    }
                    File f = new File(arg);
                    String path = f.getAbsolutePath();
                    boolean pathends1 = path.endsWith(".cpt")
                            || path.endsWith(".xls");
                    boolean pathends2 = path.endsWith(".xlsx")
                            || path.endsWith(".frm");
                    boolean pathends3 = path.endsWith(".form")
                            || path.endsWith(".cht");
                    boolean pathends4 = pathends1 || pathends2 || pathends3;
                    if (pathends4 || path.endsWith(".chart")) {
                        file = new FileFILE(f);
                    }
                }
            } else {
                file = FILEFactory.createFILE(FILEFactory.ENV_PREFIX
                        + DesignerEnvManager.getEnvManager().getLastOpenFile());
            }

            //启动时打开指定文件的接口
            DesignerStartOpenFileProcessor processor = ExtraDesignClassManager.getInstance().getSingle(DesignerStartOpenFileProcessor.XML_TAG);
            if (processor != null) {
                FILE f = processor.fileToShow();
                if (f != null) {
                    file = f;//避免null
                } else {
                    isException = true;//此时有文件nullpointer异常，执行打开空文件
                }
            }
            if (file.exists() && !isException) {
                df.openTemplate(file);
            } else {
                df.addAndActivateJTemplate();
                MutilTempalteTabPane.getInstance().setTemTemplate(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
            }
            if (OperatingSystem.isMacOS()) {
                enableFullScreenMode(df);
            }
            df.addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    df.getSelectedJTemplate().requestGridFocus();
                }
            });
            df.setVisible(true);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
            if (!isException) {
                showDesignerFrame(args, df, true);
            } else {
                System.exit(0);
            }
        }
    }


    /**
     * @param window
     */
    private void enableFullScreenMode(Window window) {
        String className = "com.apple.eawt.FullScreenUtilities";
        String methodName = "setWindowCanFullScreen";

        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, new Class<?>[]{
                    Window.class, boolean.class});
            method.invoke(null, window, true);
        } catch (Throwable t) {
            FRLogger.getLogger().error("Full screen mode is not supported");
        }
    }


    protected abstract String module2Start();

    // 收集用户信息码
    protected void collectUserInformation() {

    }
}