package com.fr.start.server;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.gui.iprogressbar.ProgressDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内置服务器启动监视器
 *
 * @author zack
 * @date 2018/8/21
 */
public class FineEmbedServerMonitor {
    private int progress;
    private static final int STEP = 1;
    /**
     * 40ms更新进度
     */
    private static final int STEP_HEARTBEAT = 40;
    private static volatile FineEmbedServerMonitor monitor;
    private static ProgressDialog progressBar = DesignerContext.getDesignerFrame().getProgressDialog();

    private FineEmbedServerMonitor() {
    }

    static {
        EventDispatcher.listen(EmbedServerEvent.AfterStop, new Listener<Null>() {
            @Override
            public void on(Event event, Null aNull) {
                getInstance().reset();
                DesignerContext.getDesignerFrame().hideProgressDialog();
            }
        });
    }

    public static FineEmbedServerMonitor getInstance() {
        if (monitor == null) {
            synchronized (FineEmbedServerMonitor.class) {
                if (monitor == null) {
                    monitor = new FineEmbedServerMonitor();
                }
            }
        }
        return monitor;
    }

    public int getProgress() {
        if (progress == progressBar.getProgressMaximum()) {
            return progress;
        } else {
            progress += STEP;
            return progress;
        }
    }

    public void setComplete() {
        this.progress = progressBar.getProgressMaximum();
    }

    public void reset() {
        this.progress = 0;
    }

    public boolean isComplete() {
        return this.progress >= progressBar.getProgressMaximum();
    }

    public void monitor() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
                new NamedThreadFactory("FineEmbedServerMonitor"));
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (isComplete()) {
                    scheduler.shutdown();
                    DesignerContext.getDesignerFrame().hideProgressDialog();
                    return;
                }
                if (!DesignerContext.getDesignerFrame().getProgressDialog().isVisible()) {
                    DesignerContext.getDesignerFrame().showProgressDialog();
                    DesignerContext.getDesignerFrame().getProgressDialog()
                            .updateLoadingText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading_Embed_Server"));
                }
                DesignerContext.getDesignerFrame().updateProgress(getProgress());
            }
        }, 0, STEP_HEARTBEAT, TimeUnit.MILLISECONDS);

    }
}
