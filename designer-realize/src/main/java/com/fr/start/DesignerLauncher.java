package com.fr.start;

import com.fr.process.FineProcess;
import com.fr.process.engine.FineJavaProcessFactory;
import com.fr.process.engine.core.FineProcessContext;
import com.fr.stable.StableUtils;

import java.io.File;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/21
 */
public class DesignerLauncher {

    private static final String BIN = "bin";
    private static final String DOT =".";
    private static final String BIN_HOME = generateBinHome();

    private static final DesignerLauncher INSTANCE = new DesignerLauncher();

    private String[] args;

    private DesignerLauncher() {

    }

    private static String generateBinHome() {
        return DOT.equals(StableUtils.getInstallHome()) ?
                DOT : StableUtils.getInstallHome() + File.separator + BIN;
    }

    public static DesignerLauncher getInstance() {
        return INSTANCE;
    }

    public void start(String[] args) {
        this.args = args;
        FineJavaProcessFactory.create().
                entry(MainDesigner.class).
                javaRuntime(DesignerJavaRuntime.getInstance().getJavaExec()).
                classPath(DesignerJavaRuntime.getInstance().getClassPath()).
                inheritJvmSettings().
                jvmSettings(DesignerJavaRuntime.getInstance().getJvmOptions()).
                arguments(args).
                directory(BIN_HOME).
                startProcess(DesignerProcessType.INSTANCE);
        DesignerSuperListener.getInstance().start();
    }

    private void beforeExit() {
        DesignerSuperListener.getInstance().stopTask();
        FineProcess process = FineProcessContext.getProcess(DesignerProcessType.INSTANCE);
        process.destroy();
    }

    public void exit() {
       beforeExit();
       DesignerSuperListener.getInstance().stop();
       System.exit(0);
    }

    public void restart() {
        beforeExit();
        start(args);
    }
}
