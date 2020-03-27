package com.fr.start;

import com.fr.common.report.ReportState;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.dialog.ErrorDialog;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.messagecollect.StartErrorMessageCollector;
import com.fr.design.mainframe.messagecollect.entity.DesignerErrorMessage;
import com.fr.design.utils.DesignUtils;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.general.ComparatorUtils;
import com.fr.process.FineProcess;
import com.fr.process.ProcessEventPipe;
import com.fr.process.engine.core.FineProcessContext;
import com.fr.process.engine.core.FineProcessEngineEvent;
import com.fr.stable.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/21
 */
public class DesignerSuperListener {

    private static final DesignerSuperListener INSTANCE =  new DesignerSuperListener();
    private static final int ONCE_DELAY = 90;
    private static final int FIXED_DELAY = 0;
    private static final int FIXED_FREQ = 2;

    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(2, new NamedThreadFactory("DesignerListener"));
    private final ExecutorService helpService = Executors.newSingleThreadExecutor( new NamedThreadFactory("DesignerSuperListener-Help"));

    private FineProcess process;
    private ScheduledFuture fixedFuture;
    private ScheduledFuture onceFuture;
    private volatile boolean cancel = false;

    private DesignerSuperListener() {

    }

    public static DesignerSuperListener getInstance() {
        return INSTANCE;
    }

    public void start() {
        process = FineProcessContext.getProcess(DesignerProcessType.INSTANCE);
        startExitListener();
        startHelpLister();
        startFrameListener();
        startFallBackListener();
    }

    private void startExitListener() {
        process.getPipe().listen(FineProcessEngineEvent.DESTROY, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                System.exit(0);
            }
        });
    }

    private void startHelpLister() {
        helpService.submit(new Runnable() {
            @Override
            public void run() {
                while (!cancel) {
                    ProcessEventPipe pipe = process.getPipe();
                    String msg = pipe.info();
                    if (ComparatorUtils.equals(ReportState.STOP.getValue(), msg)) {
                        onceFuture.cancel(false);
                    }
                    if (ComparatorUtils.equals(ReportState.ACTIVE.getValue(), msg)) {
                        startFrameListener();
                    }
                }
            }
        });
    }

    private void startFrameListener() {
        onceFuture = service.schedule(new Runnable() {
            @Override
            public void run() {
                ProcessEventPipe pipe = process.getPipe();
                pipe.fire(FineProcessEngineEvent.READY);
                if (StringUtils.isNotEmpty(pipe.info())) {
                    frameReport();
                }
            }
        }, ONCE_DELAY, TimeUnit.SECONDS);

    }

    private void frameReport() {
        DesignUtils.initLookAndFeel();
        StartErrorMessageCollector.getInstance().record(DesignerErrorMessage.UNEXCEPTED_START_FAILED.getId(),
                                                        DesignerErrorMessage.UNEXCEPTED_START_FAILED.getMessage(),
                                                        StringUtils.EMPTY);
        ErrorDialog dialog = new ErrorDialog(null,
                                             Toolkit.i18nText("Fine-Design_Error_Start_Apology_Message"),
                                             Toolkit.i18nText("Fine-Design_Error_Start_Report"),
                                             Toolkit.i18nText(DesignerErrorMessage.UNEXCEPTED_START_FAILED.getMessage())) {
            @Override
            protected void okEvent() {
                dispose();
            }

            @Override
            protected void restartEvent() {
                dispose();
                DesignerLauncher.getInstance().restart();
            }
        };
        dialog.setVisible(true);
        DesignerLauncher.getInstance().exit();
    }

    private void startFallBackListener() {
        fixedFuture = service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!process.isAlive()) {
                    fallBackReport();
                }
            }
        }, FIXED_DELAY, FIXED_FREQ, TimeUnit.SECONDS);
    }

    private void fallBackReport() {
        DesignUtils.initLookAndFeel();
        StartErrorMessageCollector.getInstance().record(DesignerErrorMessage.UNEXCEPTED_FALL_BACK.getId(),
                                                        DesignerErrorMessage.UNEXCEPTED_FALL_BACK.getMessage(),
                                                        StringUtils.EMPTY);
        ErrorDialog dialog = new ErrorDialog(null,
                                             Toolkit.i18nText("Fine-Design_Error_Fall_Back_Apology_Message"),
                                             Toolkit.i18nText("Fine-Design_Error_Fall_Back_Report"),
                                             Toolkit.i18nText(DesignerErrorMessage.UNEXCEPTED_FALL_BACK.getMessage())) {
            @Override
            protected void okEvent() {
                dispose();
            }

            @Override
            protected void restartEvent() {
                dispose();
                DesignerLauncher.getInstance().restart();
            }
        };
        dialog.setVisible(true);
        DesignerLauncher.getInstance().exit();
    }

    public void stopTask() {
        onceFuture.cancel(false);
        fixedFuture.cancel(false);
        cancel = true;
    }

    public void stop() {
        stopTask();
        service.shutdown();
        helpService.shutdown();
    }
}
