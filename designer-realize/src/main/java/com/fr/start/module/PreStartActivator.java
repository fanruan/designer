package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.fun.OemProcessor;
import com.fr.design.i18n.Toolkit;
import com.fr.design.utils.DesignUtils;
import com.fr.design.utils.DesignerPort;
import com.fr.event.EventDispatcher;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.module.Activator;
import com.fr.module.ModuleEvent;
import com.fr.stable.BuildContext;
import com.fr.stable.OperatingSystem;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.start.OemHandler;
import com.fr.start.SplashContext;
import com.fr.start.SplashStrategy;
import com.fr.start.fx.SplashFx;
import com.fr.start.jni.SplashMac;

import java.io.File;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class PreStartActivator extends Activator {

    @Override
    public void start() {

        BuildContext.setBuildFilePath("/com/fr/stable/build.properties");
        // 如果端口被占用了 说明程序已经运行了一次,也就是说，已经建立一个监听服务器，现在只要给服务器发送命令就好了
        final String[] args = findSingleton(StartupArgs.class).get();
        // 检查是否是-Ddebug = true 启动 并切换对应的端口以及环境配置文件
        checkDebugStart();
        if (DesignUtils.isStarted()) {
            DesignUtils.clientSend(args);
            FineLoggerFactory.getLogger().info("The Designer Has Been Started");
            System.exit(0);
            return;
        }

        RestartHelper.deleteRecordFilesWhenStart();

        SplashContext.getInstance().registerSplash(createSplash());

        SplashContext.getInstance().show();
        //初始化
        EventDispatcher.fire(ModuleEvent.MajorModuleStarting, Toolkit.i18nText("Fine-Design_Basic_Initializing"));
        // 完成初始化
        //noinspection ResultOfMethodCallIgnored
        CloudCenter.getInstance();

        // 创建监听服务
        DesignUtils.createListeningServer(DesignUtils.getPort(), startFileSuffix());

        initLanguage();
    }

    @Override
    public void stop() {

    }

    private void checkDebugStart() {

        if (isDebug()) {
            setDebugEnv();
        }
    }


    /**
     * 在VM options里加入-Ddebug=true激活
     *
     * @return isDebug
     */
    private boolean isDebug() {

        return ComparatorUtils.equals("true", System.getProperty("debug"));
    }


    //端口改一下，环境配置文件改一下。便于启动两个设计器，进行对比调试
    private void setDebugEnv() {

        DesignUtils.setPort(DesignerPort.DEBUG_MESSAGE_PORT);
        String debugXMlFilePath = StableUtils.pathJoin(
                ProductConstants.getEnvHome(),
                ProductConstants.APP_NAME + "Env_debug.xml"
        );
        DesignerEnvManager.setEnvFile(
                new File(debugXMlFilePath));
    }

    private void initLanguage() {
        //这两句的位置不能随便调换，因为会影响语言切换的问题
        GeneralContext.setLocale(DesignerEnvManager.getEnvManager(false).getLanguage());
    }

    private String[] startFileSuffix() {

        return new String[]{".cpt", ".xls", ".xlsx", ".frm", ".form", ".cht", ".chart"};
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
        // 这里可以开接口加载自定义启动画面
        if (OperatingSystem.isWindows()) {
            return new SplashFx();
        } else if (OperatingSystem.isMacOS()) {
            return new SplashMac();
        }
        return new SplashFx();
    }
}
