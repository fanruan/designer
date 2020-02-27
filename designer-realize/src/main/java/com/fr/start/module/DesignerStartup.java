package com.fr.start.module;


import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.TipDialog;
import com.fr.design.fun.OemProcessor;
import com.fr.design.fun.impl.GlobalListenerProviderManager;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.messagecollect.StartErrorMessageCollector;
import com.fr.design.mainframe.messagecollect.StartupMessageCollector;
import com.fr.design.mainframe.messagecollect.entity.DesignerErrorMessage;
import com.fr.design.ui.util.UIUtil;
import com.fr.design.utils.DesignUtils;
import com.fr.design.utils.DesignerPort;
import com.fr.exit.DesignerExiter;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.module.Activator;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.record.analyzer.Metrics;
import com.fr.stable.BuildContext;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.start.DesignerProcessType;
import com.fr.start.OemHandler;
import com.fr.start.ServerStarter;
import com.fr.start.SplashContext;
import com.fr.start.SplashStrategy;
import com.fr.start.common.SplashCommon;
import com.fr.start.server.FineEmbedServer;
import com.fr.value.NotNullLazyValue;
import org.jetbrains.annotations.NotNull;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * Created by juhaoyu on 2018/1/8.
 */
@EnableMetrics
public class DesignerStartup extends Activator {

    private NotNullLazyValue<StartupArgs> startupArgsValue = new NotNullLazyValue<StartupArgs>() {
        @NotNull
        @Override
        protected StartupArgs compute() {
            return findSingleton(StartupArgs.class);
        }
    };

    @Override
    public void beforeAllStart() {
        BuildContext.setBuildFilePath("/com/fr/stable/build.properties");
        // 检查是否是-Ddebug = true 启动 并切换对应的端口以及环境配置文件
        checkDebugStart();
        // 初始化look and feel
        DesignUtils.initLookAndFeel();
        if (DesignUtils.isPortOccupied()) {
            StartErrorMessageCollector.getInstance().record(DesignerErrorMessage.PORT_OCCUPIED.getId(),
                                                            DesignerErrorMessage.PORT_OCCUPIED.getMessage());
            DesignerPort.getInstance().resetPort();
        }
        if (DesignUtils.isStarted()) {
            // 如果端口被占用了 说明程序已经运行了一次,也就是说，已经建立一个监听服务器，现在只要给服务器发送命令就好了
            final String[] args = startupArgsValue.getValue().get();
            DesignUtils.clientSend(args);
            FineLoggerFactory.getLogger().info("The Designer Has Been Started");
            if (args.length == 0) {
                TipDialog dialog = new TipDialog(null,
                                                 DesignerProcessType.INSTANCE.obtain(),
                                                 Toolkit.i18nText("Fine-Design_Last_Designer_Process_Not_Exist"),
                                                 Toolkit.i18nText("Fine-Design_End_Occupied_Process"),
                                                 Toolkit.i18nText("Fine-Design_Basic_Cancel")) {
                    @Override
                    protected void endEvent() {
                        dispose();
                        DesignUtils.clientSend(new String[]{"end"});
                        RestartHelper.restart();
                    }

                    @Override
                    protected void cancelEvent() {
                        dispose();
                    }
                };
                dialog.setVisible(true);
            }
            DesignerExiter.getInstance().execute();
            return;
        }
        // 快快显示启动画面
        UIUtil.invokeAndWaitIfNeeded(new Runnable() {
            @Override
            public void run() {
                SplashContext.getInstance().registerSplash(createSplash());
                SplashContext.getInstance().show();
            }
        });
    }

    @Override
    @Metrics
    public void start() {
        startSub(PreStartActivator.class);
        startSub("parallel");
        //designer模块启动好后，查看demo
        browserDemoIfNeeded();
        startupEmbedServerIfNeeded();
    }

    private void startupEmbedServerIfNeeded() {
        if (DesignerEnvManager.getEnvManager().isEmbedServerLazyStartup()
                || FineEmbedServer.isRunning()) {
            return;
        }
        ExecutorService service = newSingleThreadExecutor(new NamedThreadFactory("FineEmbedServerStart"));
        service.submit(new Runnable() {
            @Override
            public void run() {
                FineEmbedServer.start();
            }
        });
        service.shutdown();
    }

    @Override
    public void afterAllStart() {
        GlobalListenerProviderManager.getInstance().init();
        // 启动日志收集
        StartupMessageCollector.getInstance().recordStartupLog();
    }

    private SplashStrategy createSplash() {
        OemProcessor oemProcessor = OemHandler.findOem();
        if (oemProcessor != null) {
            SplashStrategy splashStrategy = null;
            try {
                splashStrategy = oemProcessor.createSplashStrategy();
            } catch (Throwable e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            if (splashStrategy != null) {
                return splashStrategy;
            }
        }

        return new SplashCommon();
    }

    private void browserDemoIfNeeded() {

        if (startupArgsValue.getValue().isDemo()) {
            ServerStarter.browserDemoURL();
        }
    }

    /**
     * 在VM options里加入-Ddebug=true激活
     */
    private void checkDebugStart() {

        if (ComparatorUtils.equals("true", System.getProperty("debug"))) {
            setDebugEnv();
        }
    }

    /**
     * 端口改一下，环境配置文件改一下。便于启动两个设计器，进行对比调试
     */
    private void setDebugEnv() {

        DesignUtils.setPort(DesignerPort.getInstance().getDebugMessagePort());
        DesignerEnvManager.setEnvFile(new File(StableUtils.pathJoin(
                ProductConstants.getEnvHome(),
                ProductConstants.APP_NAME + "Env_debug.xml"
        )));
    }

    @Override
    public void stop() {
        // void
    }
}
